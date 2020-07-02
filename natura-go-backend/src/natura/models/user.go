package models

import (
	"database/sql"
)

type User struct {
	ID        		int64  			`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	Username  		string 			`gorm:"unique;not null" json:"username"`
	Email 	  		string 			`gorm:"unique;not null" json:"email"`
	Password  		string 			`gorm:"not null" json:"password"`
	Token	  		sql.NullString 	`gorm:"unique;type:varchar(100)" json:"token"`
	FlowerValidator	bool			`json:"flowerValidator"`
	InsectValidator bool			`json:"insectValidator"`
	Entries			[]Entry 		`gorm:"foreign_key:CreatedByID" json:"entries"`
}

func (b *User) TableName() string {
	return "user"
}