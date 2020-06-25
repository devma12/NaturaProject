package models

import(
	"time"
)

type Entry struct {
	Id        		int64  	`gorm:"primary_key, AUTO_INCREMENT"`
	Name	  		string
	Description		string	`gorm:"type:TEXT"`
	Location  		string
	IsValidated		bool	`gorm:"column:validated, type=TINYINT(1)"` 
	Date			time.Time
	CreatedBy  		User	`gorm:"foreignkey:user_id"`
	ImageID			int 	`gorm:"column:image_id"`
	Image			Image
	Discriminator	SpeciesType

}

func (b *Entry) TableName() string {
	return "entry"
}

type SpeciesType string

const(
    Flower SpeciesType = "Flower"
    Insect = "Insect"
)