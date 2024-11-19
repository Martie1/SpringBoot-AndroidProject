package com.kamark.kamark.controller;

import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthService authServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authServiceImpl.register(registerRequest);  //<AuthResponse> lub <ErrorResponse>
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return authServiceImpl.login(loginRequest); //<AuthResponse> lub <ErrorResponse>
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh") String refreshToken) {
        return authServiceImpl.refresh(refreshToken);
    }
}
