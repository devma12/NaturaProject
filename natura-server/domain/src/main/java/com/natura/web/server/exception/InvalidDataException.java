package com.natura.web.server.exception;

public class InvalidDataException extends ServerException {

    private static final Integer DEFAULT_CODE = 200;

    public InvalidDataException(Integer code) {
        super(code);
    }

    public InvalidDataException(String message) {
        super(DEFAULT_CODE, message);
    }

    protected InvalidDataException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InvalidDataException(Integer code, String message) {
        super(code, message);
    }

    public static class DuplicateDataException extends InvalidDataException {

        private final String type;
        private final String attribute;
        private final String value;

        public DuplicateDataException(String message) {
            super(201, message);
            this.type = null;
            this.attribute = null;
            this.value = null;
        }

        public DuplicateDataException(String type, String attribute, String value) {
            super(201);
            this.type = type;
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public String getMessage() {
            String message = super.getMessage();
            if (message == null) {
                message = type + " already exists with same " + attribute + ": " + value + ".";
            }
            return message;
        }
    }

    public static class AlreadyValidatedDataException extends InvalidDataException {

        public AlreadyValidatedDataException(String data) {
            super(202, data + " has already been validated.");
        }
    }

    public static class InvalidFileException extends InvalidDataException {

        public InvalidFileException(String message, Throwable cause) {
            super(203, message, cause);
        }
    }
}
