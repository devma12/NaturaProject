package com.natura.web.server.exceptions;

import com.natura.web.server.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ServerException.class})
    protected ResponseEntity<Object> handleServerException(ServerException ex, WebRequest webRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorDto, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    protected ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex, WebRequest webRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorDto, httpHeaders, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex, WebRequest webRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorDto, httpHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserAccountException.class})
    protected ResponseEntity<Object> handleUserAccountException(UserAccountException ex, WebRequest webRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ErrorDto errorDto = new ErrorDto(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorDto, httpHeaders, HttpStatus.UNAUTHORIZED);
    }
}
