package auth

import (
	"natura/models"
	"natura/repo"
	"natura/errors"
	"fmt"
	"log"
	"time"
	"strings"
	"encoding/json"
	"net/http"
	"github.com/dgrijalva/jwt-go"
  )

var jwtKey = []byte("naturaSecretKeyToGenerateToken")
var expirationDuration = 12 * time.Hour

// Struct that will be encoded to a JWT with jwt.StandardClaims as an embedded type, to provide fields like expiry time, etc.
type Claims struct {
	Username	string	`json:"username"`
	Email		string	`json:"email"`
	jwt.StandardClaims
}

func GenerateToken(user *models.User) (string, error) {
	
	// Declare the expiration time of the token
	expirationTime := time.Now().Add(expirationDuration)
	
	// Create the JWT claims, which includes the username, email and expiry time
	claims := &Claims{
		Username: user.Username,
		Email: user.Email,
		StandardClaims: jwt.StandardClaims{
			// In JWT, the expiry time is expressed as unix milliseconds
			ExpiresAt: expirationTime.Unix(),
		},
	}

	// Declare the token with the algorithm used for signing, and the claims
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	
	// Create the JWT string
	return token.SignedString(jwtKey)
}

func TokenValid(r *http.Request) error {
	// get token from request header
	tokenString := ExtractToken(r)

	// retrieve JWT token from string
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("Unexpected signing method: %v", token.Header["alg"])
		}
		return jwtKey, nil
	})
	if err != nil {
		return err
	}

	// get claims from token
	claims, ok := token.Claims.(jwt.MapClaims);
	if ok && token.Valid {
		Pretty(claims)
	}

	// Check that token is the last token stored in db
	// TO DO: later improve this - token should not be stored in db but refreshed automatically
	username := claims["username"].(string)
	var user models.User
	err = repo.GetUserByUsername(&user, username)
	if err != nil {
		return err
	}
	if user.Token.Valid && user.Token.String != tokenString {
		return &errors.JwtError{Message: "invalid token for user " + username}
	}
	
	return nil
}

func ExtractToken(r *http.Request) string {
	keys := r.URL.Query()
	token := keys.Get("token")
	if token != "" {
		return token
	}
	bearerToken := r.Header.Get("Authorization")
	if len(strings.Split(bearerToken, " ")) == 2 {
		return strings.Split(bearerToken, " ")[1]
	}
	return ""
}

//Pretty display the claims licely in the terminal
func Pretty(data interface{}) {
	b, err := json.MarshalIndent(data, "", " ")
	if err != nil {
		log.Println(err)
		return
	}

	fmt.Println(string(b))
}

func GetUsernameFromClaimsToken(r *http.Request) string {
	// get token from request header
	tokenString := ExtractToken(r)

	// retrieve JWT token from string
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("Unexpected signing method: %v", token.Header["alg"])
		}
		return jwtKey, nil
	})
	if err != nil {
		return ""
	}

	// get claims from token
	claims, ok := token.Claims.(jwt.MapClaims);
	if !ok {
		return ""
	} else {
		username := claims["username"].(string)
		return username
	}
}