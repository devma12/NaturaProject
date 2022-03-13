package com.natura.web.server.exceptions;

public class MappingException extends ServerException {

    private static final Integer DEFAULT_CODE = 400;

    protected MappingException(String message) {
        super(DEFAULT_CODE, message);
    }
}
