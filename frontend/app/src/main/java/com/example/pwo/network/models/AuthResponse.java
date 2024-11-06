package com.example.pwo.network.models;

//returned after register/login. Register automatically logs in also.
public class AuthResponse {
    private int statusCode;
    private String message;
    private String token;
    private String expirationTime;

    public AuthResponse(int statusCode, String message, String token, String expirationTime) {
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getExpirationTime() {
        return expirationTime;
    }
}
