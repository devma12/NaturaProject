package models

import(
	"time"
)

type Identification struct {
	Id        		int64  		`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	CommonName	 	string		`json:"commonName"`
	SuggestedDate	time.Time 	`json:"suggestedDate"`
	SuggestedByID	int64		`gorm:"column:user_id"`
	SuggestedBy  	User		`json:"suggestedBy"`
	ValidatedDate	time.Time 	`json:"validatedDate"`
	ValidatedByID	int64		`gorm:"column:validator_id"`
	ValidatedBy  	User		`json:"validatedBy"`
	EntryID			int64		`gorm:"column:entry_id"`
	Entry		  	Entry		`json:"entry"`
	SpeciesID		int64		`gorm:"column:species_id"`
	Species		  	Species		`json:"species"`
}

func (b *Identification) TableName() string {
	return "identification"
}