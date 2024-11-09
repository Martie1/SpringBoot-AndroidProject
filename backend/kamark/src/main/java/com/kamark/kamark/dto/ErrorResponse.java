package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//handling any error occurence, sending only 2 fields.
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
}