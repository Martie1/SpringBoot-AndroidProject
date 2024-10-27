package com.kamark.kamark.dto;

import lombok.Data;
import com.kamark.kamark.entity.User;

@Data
public class AuthResponse {
    private int statusCode;
    private String message;
    private String token;
    private String expirationTime;
}