package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleResponse {
    private int statusCode;
    private String message;
}
