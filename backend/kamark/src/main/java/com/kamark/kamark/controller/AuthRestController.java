package com.kamark.kamark.controller;

import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
      return new ResponseEntity<> (authService.register(registerRequest), HttpStatus.OK);

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        return new ResponseEntity<> (authService.login(loginRequest), HttpStatus.OK); //<AuthResponse> lub <ErrorResponse>
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh") String refreshToken) {
        return new ResponseEntity<> (authService.refresh(refreshToken), HttpStatus.OK);
    }
}
