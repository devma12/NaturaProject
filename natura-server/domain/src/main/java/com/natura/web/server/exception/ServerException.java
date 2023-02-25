package com.natura.web.server.exception;

import lombok.Getter;

@Getter
public abstract class ServerException extends Exception {

    protected final Integer code;

    protected ServerException(Integer code) {
        this.code = code;
    }

    protected ServerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    protected ServerException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
