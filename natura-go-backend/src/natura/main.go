package main

import (
	"natura/config"
	"natura/models"
	"natura/routes"
	"fmt"
	"github.com/jinzhu/gorm"
)

var err error

func main() {
	config.DB, err = gorm.Open("mysql", config.DbURL(config.BuildDBConfig()))
	if err != nil {
		fmt.Println("Status:", err)
	}
	defer config.DB.Close()
	config.DB.AutoMigrate(&models.User{})
	r := routes.SetupRouter()

	// Server on 0.0.0.0:8081
	r.Run(":8081")
}