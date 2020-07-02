package controllers

import (
	"natura/models"
	"natura/repo"
	"natura/auth"
	"fmt"
	"net/http"
	"github.com/gin-gonic/gin"
)

func DispatchGetInsect(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	param := c.Params.ByName("param1")
	switch param {
	case "all":
		GetAllInsects(c)
	case "creator":
		GetInsectsByCreator(c)
	default:
		GetInsectByID(c)
	}
}

func GetAllInsects(c *gin.Context) {
	var insect []models.Entry
	err := repo.GetAllInsects(&insect)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, insect)
	}
}

func GetInsectByID(c *gin.Context) {
	id := c.Params.ByName("param1")
	var insect models.Entry
	err := repo.GetEntryByID(&insect, id)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, insect)
	}
}

func GetInsectsByCreator(c *gin.Context) {
	userId := c.Params.ByName("param2")
	var insect []models.Entry
	err := repo.GetInsectsByCreator(&insect, userId)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, insect)
	}
}

func CreateInsect(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}
	
	var insect models.Entry
	c.BindJSON(&insect)
	err := repo.CreateEntry(&insect)
	if err != nil {
		fmt.Println(err.Error())
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, insect)
	}
}