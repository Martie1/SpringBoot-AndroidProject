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

import java.util.Optional;

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

    private AuthResponse createAuthResponse(String accessToken,String refreshToken, String message) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
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

                String accessToken = jwtUtils.generateAccessToken(user);
                String refreshToken = jwtUtils.generateRefreshToken(user);


                AuthResponse response = createAuthResponse(accessToken,refreshToken, "User Registered Successfully");
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
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            AuthResponse response = createAuthResponse(accessToken,refreshToken, "Successfully Signed In");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error during sign-in: Bad credentials"));
        }
    }
    public ResponseEntity<?> refresh(String refreshToken) {
        try {
            Integer userIdFromToken = jwtUtils.extractUserId(refreshToken);
            Optional<User> userOptional = ourUserRepo.findById(userIdFromToken);

            if (userOptional.isPresent() && jwtUtils.isRefreshTokenValid(refreshToken, userOptional.get())) {
                User user = userOptional.get();
                String newAccessToken = jwtUtils.generateAccessToken(user);

                AuthResponse response = createAuthResponse(newAccessToken, refreshToken, "Access Token refreshed");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(new ErrorResponse(401, "Invalid or expired refresh token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error refreshing token: " + e.getMessage()));
        }
    }
}