package auth

import (
    "fmt"
	"log"
	"crypto/rand"
	"golang.org/x/crypto/bcrypt"
)

func HashPassword(pwd string) string {
	
	password := []byte(pwd)

    // Use GenerateFromPassword to hash password.
    // MinCost is just an integer constant provided by the bcrypt
    // package along with DefaultCost & MaxCost. 
    // The cost can be any value you want provided it isn't lower
    // than the MinCost (4)
    hash, err := bcrypt.GenerateFromPassword(password, bcrypt.MinCost)
    if err != nil {
        fmt.Println(err)
    }
    // GenerateFromPassword returns a byte slice so we need to
    // convert the bytes to a string and return it
    return string(hash)
}

func ComparePasswords(hashedPwd string, plainPwd string) bool {
    // Since we'll be getting the hashed password from the DB it
    // will be a string so we'll need to convert it to a byte slice
	byteHash := []byte(hashedPwd)
	bytePwd := []byte(plainPwd)
    err := bcrypt.CompareHashAndPassword(byteHash, bytePwd)
    if err != nil {
        log.Println(err)
        return false
    }
    
    return true
}

// temporary function to get unique uuid to put in user unique token
func GetUUID() string {
	b := make([]byte, 16)
	_, err := rand.Read(b)
	if err != nil {
		log.Fatal(err)
	}
	uuid := fmt.Sprintf("%x-%x-%x-%x-%x",
		b[0:4], b[4:6], b[6:8], b[8:10], b[10:])
	fmt.Println(uuid)

	return uuid
}
