package controllers

import (
	"natura/models"
	"natura/repo"
	"fmt"
	"bytes"
	"io"
	"net/http"
	"github.com/gin-gonic/gin"
)

func Upload(c *gin.Context) {
	file, header, err := c.Request.FormFile("file")
	defer file.Close()
	if err != nil {
		fmt.Println(err.Error())
		c.AbortWithError(http.StatusNotAcceptable, err)
	}

	
	buf := bytes.NewBuffer(nil)
	if _, err := io.Copy(buf, file); err != nil {
		fmt.Println(err.Error())
		c.AbortWithError(http.StatusNotAcceptable, err)
	}

	data := buf.Bytes()
	contentType := http.DetectContentType(data)

	var image models.Image
	image.Name = header.Filename
	image.Type = contentType
	image.Data = data

	errDB := repo.UploadImage(&image)
	if errDB != nil {
		fmt.Println(errDB.Error())
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, image)
	}
}

func Download(c *gin.Context) {
	id := c.Params.ByName("id")
	var image models.Image
	err := repo.GetImageByID(&image, id)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, image)
	}
}