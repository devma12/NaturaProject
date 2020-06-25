package controllers

import (
	"natura/models"
	"natura/repo"
	"fmt"
	"net/http"
	"github.com/gin-gonic/gin"
)

func DispatchGetFlower(c *gin.Context) {
	param := c.Params.ByName("param1")
	switch param {
	case "all":
		GetAllFlowers(c)
	case "creator":
		GetFlowersByCreator(c)
	default:
		GetFlowerByID(c)
	}
}

func GetAllFlowers(c *gin.Context) {
	var flower []models.Entry
	err := repo.GetAllFlowers(&flower)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, flower)
	}
}

func CreateFlower(c *gin.Context) {
	var flower models.Entry
	c.BindJSON(&flower)
	err := repo.CreateEntry(&flower)
	if err != nil {
		fmt.Println(err.Error())
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, flower)
	}
}

func GetFlowerByID(c *gin.Context) {
	id := c.Params.ByName("param1")
	var flower models.Entry
	err := repo.GetEntryByID(&flower, id)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, flower)
	}
}

func GetFlowersByCreator(c *gin.Context) {
	userId := c.Params.ByName("param2")
	var flower []models.Entry
	err := repo.GetFlowersByCreator(&flower, userId)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, flower)
	}
}