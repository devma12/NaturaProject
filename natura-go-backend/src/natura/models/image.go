package models

type Image struct {
	Id        		int64  	`gorm:"primary_key, AUTO_INCREMENT"`
	Name	  		string
	Type			string
	Data			[]byte	`gorm:"type:MEDIUMBLOB"`
}