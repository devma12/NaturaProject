package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends ServerException {

  public DataNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public DataNotFoundException(Class<?> type, String field, Object value) {
    super(HttpStatus.NOT_FOUND, type.getSimpleName() + " not found for " + field + " = " + value.toString());
  }
}
