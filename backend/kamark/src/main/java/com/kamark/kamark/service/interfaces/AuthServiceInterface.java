package com.kamark.kamark.service.interfaces;


import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthServiceInterface {

    AuthResponse register(RegisterRequest registrationRequest);

    AuthResponse login(LoginRequest loginRequest);


    AuthResponse refresh(String refreshToken);
}