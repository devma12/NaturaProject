package repo

import (
	"natura/config"
	"natura/models"
	_ "github.com/go-sql-driver/mysql"
)

func GetImageByID(image *models.Image, id string) (err error) {
	if err = config.DB.Where("id = ?", id).First(image).Error; err != nil {
		return err
	}
	return nil
}

func UploadImage(image *models.Image) (err error) {
	if err = config.DB.Create(image).Error; err != nil {
		return err
	}
	return nil
}

func DeleteImage(image *models.Image, id string) (err error) {
	config.DB.Where("id = ?", id).Delete(image)
	return nil
}