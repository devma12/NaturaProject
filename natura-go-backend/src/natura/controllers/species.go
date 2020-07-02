package controllers

import (
	"natura/models"
	"natura/repo"
	"natura/auth"
	"net/http"
	"github.com/gin-gonic/gin"
)

func GetAllSpecies(c *gin.Context) {

	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var species []models.Species
	err := repo.GetAllSpecies(&species)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, species)
	}
}

func GetSpeciesByType(c *gin.Context) {
	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	speciesType := c.Params.ByName("type")
	var species []models.Species
	err := repo.GetSpeciesByType(&species, speciesType)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, species)
	}
}

func CreateSpecies(c *gin.Context) {
	authErr := auth.TokenValid(c.Request)
	if authErr != nil {
	   c.JSON(http.StatusUnauthorized, "invalid token")
	   return
	}

	var species models.Species
	c.BindJSON(&species)
	err := repo.CreateSpecies(&species)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, species)
	}
}