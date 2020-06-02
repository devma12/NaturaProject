package com.natura.web.server.exceptions;

import com.natura.web.server.entities.Species;
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

    public static class InvalidAccountDataException extends UserAccountException {

        public InvalidAccountDataException(String message) {
            super(HttpStatus.UNAUTHORIZED, "Invalid " + message);
        }

    }

    public static class ValidationPermissionException extends UserAccountException {

        public ValidationPermissionException(String message) {
            super(HttpStatus.UNAUTHORIZED, message);
        }

        public ValidationPermissionException(String user, Species.Type type) {
            super(HttpStatus.UNAUTHORIZED, "User " + user + " has no permission to validate " + type);
        }

    }
}
