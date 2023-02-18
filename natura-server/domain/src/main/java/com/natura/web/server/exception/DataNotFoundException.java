package com.natura.web.server.exception;

public class DataNotFoundException extends ServerException {

    private static final Integer DEFAULT_CODE = 100;

    public DataNotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }

    public DataNotFoundException(Class<?> type, String field, Object value) {
        super(DEFAULT_CODE, type.getSimpleName() + " not found for " + field + " = " + value.toString());
    }
}
