package models

type User struct {
	ID        		int64  	`gorm:"primary_key, AUTO_INCREMENT"`
	Username  		string 	`gorm:"unique;not null"`
	Email 	  		string 	`gorm:"unique;not null"`
	Password  		string 	`gorm:"not null"`
	Token	  		string 	`gorm:"unique;type:varchar(100)"`
	FlowerValidator	bool
	InsectValidator bool
}

func (b *User) TableName() string {
	return "user"
}