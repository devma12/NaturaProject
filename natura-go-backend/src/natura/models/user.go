package models

import (
	"database/sql"
	"database/sql/driver"
	"encoding/json"
	"reflect"
)

type User struct {
	ID        		int64  			`gorm:"primary_key, AUTO_INCREMENT" json:"id"`
	Username  		string 			`gorm:"unique;not null" json:"username"`
	Email 	  		string 			`gorm:"unique;not null" json:"email"`
	Password  		string 			`gorm:"not null" json:"password"`
	Token	  		NullString 		`gorm:"unique;type:varchar(100)" json:"token"`
	FlowerValidator	bool			`json:"flowerValidator"`
	InsectValidator bool			`json:"insectValidator"`
	Entries			[]Entry 		`gorm:"foreign_key:CreatedByID" json:"entries"`
}

func (b *User) TableName() string {
	return "user"
}

//NullString is a wrapper around sql.NullString
type NullString sql.NullString

// Scan implements the Scanner interface for NullString
func (ns *NullString) Scan(value interface{}) error {
	var s sql.NullString
	if err := s.Scan(value); err != nil {
		return err
	}

	// if nil then make Valid false
	if reflect.TypeOf(value) == nil {
		*ns = NullString{s.String, false}
	} else {
		*ns = NullString{s.String, true}
	}

	return nil
}

// Value implements the driver Valuer interface.
func (ns NullString) Value() (driver.Value, error) {
	if !ns.Valid {
		return nil, nil
	}
	return ns.String, nil
}

//MarshalJSON method is called by json.Marshal, whenever it is of type NullString
func (ns NullString) MarshalJSON() ([]byte, error) {
    if !ns.Valid {
        return []byte("null"), nil
    }
    return json.Marshal(ns.String)
}

//MarshalJSON method is called by json.Marshal, whenever it is of type NullString
func (ns *NullString) UnmarshalJSON(data []byte) error {
	// Unmarshalling into a pointer will let us detect null
    var s *string
    if err := json.Unmarshal(data, &s); err != nil {
        return err
    }
    if s != nil {
        ns.Valid = true
        ns.String = *s
    } else {
        ns.Valid = false
    }
    return nil
}