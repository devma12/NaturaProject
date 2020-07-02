package repo

import (
	"natura/config"
	"natura/models"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

// Fetch all user data
func GetAllUsers(user *[]models.User) (err error) {
	if err = config.DB.Find(user).Error; err != nil {
		return err
	}
	return nil
}

// Insert new user
func CreateUser(user *models.User) (err error) {
	if err = config.DB.Create(user).Error; err != nil {
		return err
	}
	return nil
}

// Fetch only one user by Id
func GetUserByID(user *models.User, id int64) (err error) {
	if err = config.DB.Where("id = ?", id).First(user).Error; err != nil {
		return err
	}
	return nil
}

// Fetch only one user by Username
func GetUserByUsername(user *models.User, username string) (err error) {
	if err = config.DB.Where("username = ?", username).First(user).Error; err != nil {
		return err
	}
	return nil
}

//UpdateUser ... Update user
func UpdateUser(user *models.User, id int64) (err error) {
	fmt.Println(user)
	if err = config.DB.Save(user).Error; err != nil {
		return err
	}
	return nil
}

//DeleteUser ... Delete user
func DeleteUser(user *models.User, id int64) (err error) {
	if err = config.DB.Where("id = ?", id).Delete(user).Error; err != nil {
		return err
	}	
	return nil
}