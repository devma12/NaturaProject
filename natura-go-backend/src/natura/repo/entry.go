package repo

import (
	"natura/config"
	"natura/models"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

func GetEntryByID(entry *models.Entry, id string) (err error) {
	if err = config.DB.Preload("CreatedBy").Where("id = ?", id).First(entry).Error; err != nil {
		return err
	}
	return nil
}

func CreateEntry(entry *models.Entry) (err error) {
	if err = config.DB.Create(entry).Error; err != nil {
		return err
	}
	return nil
}

func UpdateEntry(entry *models.Entry, id string) (err error) {
	fmt.Println(entry)
	config.DB.Save(entry)
	return nil
}

func DeleteEntry(entry *models.Entry, id string) (err error) {
	config.DB.Where("id = ?", id).Delete(entry)
	return nil
}