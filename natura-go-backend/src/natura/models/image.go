package models

type Image struct {
	Id        		int64  	`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	Name	  		string	`json:"name"`
	Type			string	`json:"type"`
	Data			[]byte	`gorm:"type:MEDIUMBLOB" json:"data"`
}