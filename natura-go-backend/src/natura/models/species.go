package models

type Species struct {
	Id        		int64  		`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	CommonName	 	string		`json:"commonName"`
	ScientificName 	string		`json:"scientificName"`
	Type			SpeciesType	`json:"type"`
	Order			string		`gorm:"column:classification_order" json:"order"`
	Family  		string		`json:"family"`
	IsValidated		bool		`gorm:"column:validated, type=TINYINT(1)" json:"validated"` 
	HabitatType		string		`json:"habitatType"`
	Phenologies		[]Phenology	`gorm:"foreignkey:SpeciesID" json:"phenologies"`
}

func (b *Species) TableName() string {
	return "species"
}

type SpeciesType string

const(
    Flower SpeciesType = "Flower"
    Insect = "Insect"
)