package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends ServerException {

    public InvalidDataException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, "Invalid " + message);
    }
}
