package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;

public class UserAccountException extends ServerException {

    public UserAccountException(HttpStatus status, String message) {
        super(status, message);
    }

    public static class MandatoryUserDetailException extends UserAccountException {

        public MandatoryUserDetailException(String message) {
            super(HttpStatus.UNAUTHORIZED, message + " is required to create an account.");
        }
    }

    public static class DuplicateAccountException extends UserAccountException {

        public DuplicateAccountException(String message) {
            super(HttpStatus.UNAUTHORIZED,  "An account already exists with same " + message);
        }
    }

    public static class AuthenticationException extends UserAccountException {

        public AuthenticationException(String message) {
            super(HttpStatus.UNAUTHORIZED,  message);
        }
    }
}
