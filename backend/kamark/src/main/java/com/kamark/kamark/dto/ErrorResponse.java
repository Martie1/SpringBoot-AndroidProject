package com.kamark.kamark.dto;

import lombok.Data;

//handling any error occurence, sending only 2 fields.
@Data
public class ErrorResponse {
    private int statusCode;
    private String message;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}