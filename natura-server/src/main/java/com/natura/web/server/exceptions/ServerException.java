package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class ServerException extends Exception {

    protected final HttpStatus status;

    protected ServerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }


    protected ServerException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public ResponseStatusException responseStatus() {
        return new ResponseStatusException(status, this.getMessage(), this.getCause());
    }
}
