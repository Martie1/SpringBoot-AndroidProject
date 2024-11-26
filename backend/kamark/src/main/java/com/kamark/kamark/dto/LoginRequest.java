package com.kamark.kamark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "{NotBlank.loginRequest.email}")
    @Email(message = "{Email.loginRequest.email}")
    private String email;

    @NotBlank(message = "{NotBlank.loginRequest.password}")
    private String password;
}