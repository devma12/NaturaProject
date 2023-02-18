package com.natura.web.server.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private Integer code;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }
}
