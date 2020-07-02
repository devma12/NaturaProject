package controllers

import (
	"natura/models"
	"natura/repo"
	"natura/errors"
	"natura/auth"
	"fmt"
	"net/http"
	"strconv"
	"github.com/gin-gonic/gin"
)

// Dispatch GET requests : check if requesting all users or user by id
func DispatchGetUser(c *gin.Context) {
	param := c.Params.ByName("id")
	if param == "all" {
		GetUsers(c)
	} else {
		GetUserByID(c)
	}
}

// Get all users
func GetUsers(c *gin.Context) {
	var user []models.User
	err := repo.GetAllUsers(&user)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, user)
	}
}

// Create new user in db
func RegisterUser(c *gin.Context) {
	var user models.User

	// get user details from request params
	q := c.Request.URL.Query()
	username := q.Get("username")
	email := q.Get("email")
	password := q.Get("password")

	var err *errors.MandatoryUserDetailsError
	if len(username) == 0 {
		err = &errors.MandatoryUserDetailsError{username}
	} else if len(email) == 0 {
		err = &errors.MandatoryUserDetailsError{email}
	} else if len(password) == 0 {
		err = &errors.MandatoryUserDetailsError{password}
	} else {
		err = nil
	}

	if err != nil {
		fmt.Println(err.Error())
		c.AbortWithError(http.StatusUnauthorized, err)
	} else {
		user.Username = username
		user.Email = email
		// crypt user password
		hashedPassword := auth.HashPassword(password)
		user.Password = hashedPassword

		// generate unique token
		token, err := auth.GenerateToken(&user)
		if err != nil {
			fmt.Println(err.Error())
			c.AbortWithError(http.StatusInternalServerError, err)
		} else {
			user.Token.String = token
			user.Token.Valid = true
			// store user in db
			err := repo.CreateUser(&user)
			if err != nil {
				fmt.Println(err.Error())
				c.AbortWithError(http.StatusBadRequest, err)
			} else {
				c.JSON(http.StatusOK, user)
			}
		}

	}
}

// Get the user by id
func GetUserByID(c *gin.Context) {
	var user models.User
	strId := c.Params.ByName("id")
	id, err := strconv.ParseInt(strId, 10, 64)
	if err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		err := repo.GetUserByID(&user, id)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			c.JSON(http.StatusOK, user)
		}
	}
	

}

// Login user
func Login(c *gin.Context) {
	var user models.User

	// get username and password from request params
	username := c.Query("username")
	password := c.Query("password")

	if len(username) == 0 {
		c.AbortWithStatus(http.StatusUnauthorized)
	} else {
		// look for user by username
		err := repo.GetUserByUsername(&user, username)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			// compare user password
			if auth.ComparePasswords(user.Password, password) {
				// generate token
				token, err := auth.GenerateToken(&user)
				if err != nil {
					fmt.Println(err.Error())
					c.AbortWithError(http.StatusInternalServerError, err)
				} else {
					user.Token.String = token
					user.Token.Valid = true
					// store user in db
					err := repo.UpdateUser(&user, user.ID)
					if err != nil {
						fmt.Println(err.Error())
						c.AbortWithError(http.StatusBadRequest, err)
					} else {
						c.JSON(http.StatusOK, user)
					}
				}
			} else {
				c.AbortWithStatus(http.StatusUnauthorized)
			}
			
		}
	}

}

// Logout user
func Logout(c *gin.Context) {
	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	// Get username from token
	username:= auth.GetUsernameFromClaimsToken(c.Request)
	if username == "" {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		var user models.User
		err := repo.GetUserByUsername(&user, username)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
			return
		}
		// reset user token
		user.Token.String = ""
		user.Token.Valid = false
		err = repo.UpdateUser(&user, user.ID)
		if err != nil {
			fmt.Println(err.Error())
			c.AbortWithError(http.StatusBadRequest, err)
			return
		}
		c.AbortWithStatus(http.StatusOK)
	}

}

// Update user email
func UpdateUserEmail(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var user models.User

	// get user to update by id
	strId := c.Params.ByName("id")
	id, err := strconv.ParseInt(strId, 10, 64)
	if err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		err := repo.GetUserByID(&user, id)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			// get new email from request body
			email, _ := c.GetRawData()
			user.Email  = string(email)
			err = repo.UpdateUser(&user, id)
			if err != nil {
				c.AbortWithStatus(http.StatusNotFound)
			} else {
				c.JSON(http.StatusOK, user)
			}
		}
	}
}

// Update user password
func UpdateUserPassword(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var user models.User

	oldPwd := c.Query("old")
	newPwd := c.Query("new")
	strId := c.Params.ByName("id")
	id, err := strconv.ParseInt(strId, 10, 64)
	if err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		// get user to update by id
		err := repo.GetUserByID(&user, id)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			// check old password
			if !auth.ComparePasswords(user.Password, oldPwd) {
				c.AbortWithStatus(http.StatusUnauthorized)
			} else {
				// update password
				user.Password  = newPwd
				err = repo.UpdateUser(&user, id)
				if err != nil {
					c.AbortWithStatus(http.StatusNotFound)
				} else {
					c.JSON(http.StatusOK, user)
				}
			}
		}
	}
}

// Update the user information
func UpdateUser(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var user models.User
	strId := c.Params.ByName("id")
	id, err := strconv.ParseInt(strId, 10, 64)
	if err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		err := repo.GetUserByID(&user, id)
		if err != nil {
			c.JSON(http.StatusNotFound, user)
		}
		c.BindJSON(&user)
		err = repo.UpdateUser(&user, id)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			c.JSON(http.StatusOK, user)
		}
	}
}

// Create User
func CreateUser(c *gin.Context) {
	var user models.User
	c.BindJSON(&user)
	err := repo.CreateUser(&user)
	if err != nil {
		fmt.Println(err.Error())
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, user)
	}
}

// Delete the user
func DeleteUser(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var user models.User
	strId := c.Params.ByName("id")
	id, err := strconv.ParseInt(strId, 10, 64)
	if err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		err := repo.DeleteUser(&user, id)
		if err != nil {
			c.AbortWithStatus(http.StatusNotFound)
		} else {
			c.JSON(http.StatusOK, gin.H{"id" + strId: "is deleted"})
		}
	}
}