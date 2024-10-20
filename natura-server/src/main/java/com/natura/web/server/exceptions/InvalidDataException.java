package com.natura.web.server.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends ServerException {

  public InvalidDataException(String message) {
    super(HttpStatus.NOT_ACCEPTABLE, message);
  }

  public static class DuplicateDataException extends InvalidDataException {

    public DuplicateDataException(String message) {
      super(message);
    }

    public DuplicateDataException(String type, String attribute, String value) {
      super(type + " already exists with same " + attribute + ": " + value + ".");
    }
  }

  public static class AlreadyValidatedDataException extends InvalidDataException {

    public AlreadyValidatedDataException(String data) {
      super(data + " has already been validated.");
    }
  }
}
