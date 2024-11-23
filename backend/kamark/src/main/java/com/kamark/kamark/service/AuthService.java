package com.kamark.kamark.service;

import com.kamark.kamark.controller.validators.LoginValidator;
import com.kamark.kamark.controller.validators.RegisterValidator;
import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.ErrorResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements AuthServiceInterface {

    private final UserRepository ourUserRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RegisterValidator registerValidator;
    private final LoginValidator loginValidator;

    public AuthService(UserRepository ourUserRepo, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RegisterValidator registerValidator, LoginValidator loginValidator) {
        this.ourUserRepo = ourUserRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.registerValidator = registerValidator;
        this.loginValidator = loginValidator;
    }


    private AuthResponse createAuthResponse(String accessToken,String refreshToken, String message) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpirationTime("Access Token expires in 1 hour (for Swagger purposes) (), Refresh Token expires in 7 days");
        response.setMessage(message);
        response.setStatusCode(200);
        return response;
    }

    public ResponseEntity<?> register(RegisterRequest registrationRequest) {
        try {
            String usernameValidationResult = registerValidator.validateUsername(registrationRequest.getUsername());
            if (usernameValidationResult != "ok") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, usernameValidationResult));
            }

            String emailValidationResult = registerValidator.validateEmail(registrationRequest.getEmail());
            if (emailValidationResult != "ok") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, emailValidationResult));
            }

            String passwordValidationResult = registerValidator.validatePassword(
                    registrationRequest.getPassword(),
                    registrationRequest.getUsername()
            );

            if (passwordValidationResult != "ok") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, passwordValidationResult));
            }
            if (ourUserRepo.existsByEmail(registrationRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "Email is already taken."));
            }
            if (ourUserRepo.existsByUsername(registrationRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, "Username is already taken."));
            }




            UserEntity user = new UserEntity();
            user.setEmail(registrationRequest.getEmail());
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setStatus("alive");
            user.setRole("USER");

            UserEntity ourUserResult = ourUserRepo.save(user);
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
            String emailValidationResult = loginValidator.validateEmail(loginRequest.getEmail());
            if (emailValidationResult != "ok") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, emailValidationResult));
            }
            String passwordValidationResult = loginValidator.validatePassword(loginRequest.getPassword());
            if (passwordValidationResult != "ok") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, passwordValidationResult));
            }


            UserEntity user = ourUserRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail())
            );
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            AuthResponse response = createAuthResponse(accessToken,refreshToken, "Successfully Signed In");
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Email doesn't exist"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Wrong password"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error during sign-in: " + e.getMessage()));
        }
    }
    public ResponseEntity<?> refresh(String refreshToken) {
        try {
            Integer userIdFromToken = jwtUtils.extractUserId(refreshToken);
            Optional<UserEntity> userOptional = ourUserRepo.findById(userIdFromToken);

            if (userOptional.isPresent() && jwtUtils.isRefreshTokenValid(refreshToken, userOptional.get())) {
                UserEntity user = userOptional.get();
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