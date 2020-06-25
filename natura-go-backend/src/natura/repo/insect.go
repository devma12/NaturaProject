package repo

import (
	"natura/config"
	"natura/models"
	_ "github.com/go-sql-driver/mysql"
)

func GetAllInsects(insect *[]models.Entry) (err error) {
	if err = config.DB.Where("discriminator = ?", models.Insect).Find(insect).Error; err != nil {
		return err
	}
	return nil
}

func GetInsectsByCreator(insect *[]models.Entry, id string) (err error) {
	if err = config.DB.Where("discriminator = ?", models.Insect).Where("user_id = ?", id).Find(insect).Error; err != nil {
		return err
	}
	return nil
}