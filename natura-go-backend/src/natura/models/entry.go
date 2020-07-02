package models

import(
	"time"
)

type Entry struct {
	Id        		int64  	`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	Name	  		string	`json:"name"`
	Description		string	`gorm:"type:TEXT" json:"description"`
	Location  		string	`json:"location"`
	IsValidated		bool	`gorm:"column:validated, type=TINYINT(1)" json:"validated"` 
	Date			time.Time `json:"date"`
	CreatedByID		int64	`gorm:"column:user_id"`
	CreatedBy  		User	`json:"createdBy"`
	ImageID			int64 	`gorm:"column:image_id"`
	Image			Image	`json:"image"`
	Discriminator	SpeciesType

}

func (b *Entry) TableName() string {
	return "entry"
}