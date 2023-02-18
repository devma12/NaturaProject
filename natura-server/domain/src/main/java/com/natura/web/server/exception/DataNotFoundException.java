package com.natura.web.server.exception;

public class DataNotFoundException extends ServerException {

    private static final Integer DEFAULT_CODE = 100;

    private final Class<?> type;
    private final String field;
    private final Object value;

    public DataNotFoundException(String message) {
        super(DEFAULT_CODE, message);
        this.type = null;
        this.field = null;
        this.value = null;
    }

    public DataNotFoundException(Class<?> type, String field, Object value) {
        super(DEFAULT_CODE);
        this.type = type;
        this.field = field;
        this.value = value;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && type != null && field != null && value != null) {
            message =  String.format("%s not found for %s = %s", type.getSimpleName(), field, value);
        }
        return message;
    }
}
