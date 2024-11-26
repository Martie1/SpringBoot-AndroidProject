package com.kamark.kamark.service;

import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.entity.UserEntity;
import com.kamark.kamark.exceptions.UserAlreadyExistsException;
import com.kamark.kamark.repository.UserRepository;
import com.kamark.kamark.service.interfaces.AuthServiceInterface;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class AuthService implements AuthServiceInterface {

    private final UserRepository ourUserRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthService(UserRepository ourUserRepo, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.ourUserRepo = ourUserRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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

    public AuthResponse register(RegisterRequest registrationRequest) {
            if (ourUserRepo.existsByEmail(registrationRequest.getEmail())) {
                throw new UserAlreadyExistsException("Email already taken");
            }
            if (ourUserRepo.existsByUsername(registrationRequest.getUsername())) {
                throw new UserAlreadyExistsException("Username already taken");  }

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
                return response;
            } else {
                throw new RuntimeException("Registration failed");
            }
    }

    public AuthResponse login(LoginRequest loginRequest) {

            UserEntity user = ourUserRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail())
            );
            if ("deactivated".equalsIgnoreCase(user.getStatus())) {
                    throw new UsernameNotFoundException("This account has been deactivated");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            AuthResponse response = createAuthResponse(accessToken,refreshToken, "Successfully Signed In");
            return response;
    }
    public AuthResponse refresh(String refreshToken) {
            Integer userIdFromToken = jwtUtils.extractUserIdFromRefreshToken(refreshToken);

            UserEntity user = ourUserRepo.findById(userIdFromToken).orElseThrow(
                    () -> new NotFoundException("User not found with id: " + userIdFromToken)
            );
            String newAccessToken = jwtUtils.generateAccessToken(user);
            AuthResponse response = createAuthResponse(newAccessToken, refreshToken, "Access Token refreshed");
            return response;
    }

}