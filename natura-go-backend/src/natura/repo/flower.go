package repo

import (
	"natura/config"
	"natura/models"
	_ "github.com/go-sql-driver/mysql"
)

func GetAllFlowers(flower *[]models.Entry) (err error) {
	if err = config.DB.Where("discriminator = ?", models.Flower).Find(flower).Error; err != nil {
		return err
	}
	return nil
}

func GetFlowersByCreator(flower *[]models.Entry, id string) (err error) {
	if err = config.DB.Where("discriminator = ?", models.Flower).Where("user_id = ?", id).Find(flower).Error; err != nil {
		return err
	}
	return nil
}