package com.natura.web.server.exception;

public class InvalidDataException extends ServerException {

    private static final Integer DEFAULT_CODE = 200;

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

        public DuplicateDataException(String message) {
            super(201, message);
        }

        public DuplicateDataException(String type, String attribute, String value) {
            super(201, type + " already exists with same " + attribute + ": " + value + ".");
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
