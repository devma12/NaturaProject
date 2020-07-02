package repo

import (
	"natura/config"
	"natura/models"
	_ "github.com/go-sql-driver/mysql"
)

func GetAllSpecies(species *[]models.Species) (err error) {
	if err = config.DB.Find(species).Error; err != nil {
		return err
	}
	return nil
}

func GetSpeciesByID(species *models.Species, id string) (err error) {
	if err = config.DB.Where("id = ?", id).First(species).Error; err != nil {
		return err
	}
	return nil
}

func GetSpeciesByType(species *[]models.Species, speciesType string) (err error) {
	if err = config.DB.Where("type = ?", speciesType).Find(species).Error; err != nil {
		return err
	}
	return nil
}

func CreateSpecies(species *models.Species) (err error) {
	if err = config.DB.Create(species).Error; err != nil {
		return err
	}
	return nil
}