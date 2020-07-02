package models

import(
	"time"
)

type Phenology struct {
	Id		int64  		`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	Start	time.Month	`json:"start"`
	End		time.Month	`json:"end"`
	SpeciesID	int64	
}

func (b *Phenology) TableName() string {
	return "phenology"
}