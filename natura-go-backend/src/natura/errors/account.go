package errors

import (
	"fmt"
)

type MandatoryUserDetailsError struct {
	Message string
}

func (e *MandatoryUserDetailsError) Error() string {
	return fmt.Sprintf("%s is required to create an account.",
		e.Message)
}


type JwtError struct {
	Message string
}

func (e *JwtError) Error() string {
	return fmt.Sprintf("%s",
		e.Message)
}