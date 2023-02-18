package com.natura.web.server.exception;

import com.natura.web.server.model.SpeciesType;

public class UserAccountException extends ServerException {

    private static final Integer DEFAULT_CODE = 300;

    public UserAccountException() {super(DEFAULT_CODE);}

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

        private final String user;
        private final SpeciesType speciesType;

        public ValidationPermissionException(String message) {
            super(message);
            this.user = null;
            this.speciesType = null;
        }

        public ValidationPermissionException(String user, SpeciesType type) {
            super();
            this.user = user;
            this.speciesType = type;
        }

        @Override
        public String getMessage() {
            String message = super.getMessage();
            if (message == null && this.speciesType != null) {
                message = String.format("User %s has no permission to validate %s", user, speciesType);
            }
            return message;
        }
    }
}
