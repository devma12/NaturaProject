package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;

public class MandatoryDataAccountException extends ServerException {

    public MandatoryDataAccountException(String message) {
        super(HttpStatus.UNAUTHORIZED, message + " is required to create an account.");
    }
}
