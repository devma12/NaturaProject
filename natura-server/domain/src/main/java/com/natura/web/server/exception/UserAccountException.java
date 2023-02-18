package com.natura.web.server.exception;

import com.natura.web.server.model.SpeciesType;

public class UserAccountException extends ServerException {

    private static final Integer DEFAULT_CODE = 300;

    public UserAccountException(String message) {
        super(DEFAULT_CODE, message);
    }

    public static class MandatoryUserDetailException extends UserAccountException {

        public MandatoryUserDetailException(String message) {
            super(message + " is required to create an account.");
        }
    }

    public static class DuplicateAccountException extends UserAccountException {

        public DuplicateAccountException(String message) {
            super("An account already exists with same " + message);
        }
    }

    public static class AuthenticationException extends UserAccountException {

        public AuthenticationException(String message) {
            super(message);
        }
    }

    public static class InvalidAccountDataException extends UserAccountException {

        public InvalidAccountDataException(String message) {
            super("Invalid " + message);
        }

    }

    public static class ValidationPermissionException extends UserAccountException {

        public ValidationPermissionException(String message) {
            super(message);
        }

        public ValidationPermissionException(String user, SpeciesType type) {
            super("User " + user + " has no permission to validate " + type);
        }

    }
}
