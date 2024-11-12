package com.example.pwo.network.models;

//returned after register/login.
public class AuthResponse {
    private int statusCode;
    private String message;
    private String accessToken;
    private String refreshToken;
    private String expirationTime;

    public AuthResponse(int statusCode, String message, String accessToken, String refreshToken, String expirationTime) {
        this.statusCode = statusCode;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {return refreshToken;}

    public String getExpirationTime() {
        return expirationTime;
    }
}
