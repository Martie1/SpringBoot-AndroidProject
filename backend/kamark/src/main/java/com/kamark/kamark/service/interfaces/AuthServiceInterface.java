package com.kamark.kamark.service.interfaces;


import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthServiceInterface {

    ResponseEntity<?> register(RegisterRequest registrationRequest);

    ResponseEntity<?> login(LoginRequest loginRequest);


    ResponseEntity<?> refresh(String refreshToken);
}