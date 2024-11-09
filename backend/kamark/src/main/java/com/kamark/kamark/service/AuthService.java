package com.kamark.kamark.service;

import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {

    @Autowired
    private UserRepository ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    private AuthResponse createAuthResponse(String token, String message) {
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpirationTime("24Hr");
        response.setMessage(message);
        response.setStatusCode(200);
        return response;
    }

    public ResponseEntity<?> register(RegisterRequest registrationRequest) {
        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setStatus("alive");
            user.setRole("USER");

            User ourUserResult = ourUserRepo.save(user);
            if (ourUserResult != null && ourUserResult.getId() > 0) {
                String jwt = jwtUtils.generateToken(ourUserResult);


                AuthResponse response = createAuthResponse(jwt, "User Registered Successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(500).body(new ErrorResponse(500, "Failed to register user."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error during registration: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            User user = ourUserRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail())
            );
            String jwt = jwtUtils.generateToken(user);

            AuthResponse response = createAuthResponse(jwt, "Successfully Signed In");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error during sign-in: Bad credentials"));
        }
    }
}