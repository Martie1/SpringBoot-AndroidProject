package com.example.pwo.network.models;

public class SimpleResponse {
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int statusCode;
    private String message;

    public SimpleResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
