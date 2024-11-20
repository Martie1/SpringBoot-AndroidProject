package com.kamark.kamark.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private int statusCode;
    private String message;
    private String accessToken;
    private String refreshToken;
    private String expirationTime;
}