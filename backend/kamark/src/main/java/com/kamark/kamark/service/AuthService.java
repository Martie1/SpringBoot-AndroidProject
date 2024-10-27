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

    public ResponseEntity<?> register(RegisterRequest registrationRequest) {
        try {
            User ourUsers = new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setUsername(registrationRequest.getUsername());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());

            // Zapisujemy użytkownika w bazie danych
            User ourUserResult = ourUserRepo.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getId() > 0) {
                // Jeśli zapis się powiódł, generujemy token
                String jwt = jwtUtils.generateToken(ourUserResult);

                // Tworzymy AuthResponse z tokenem
                AuthResponse response = new AuthResponse();
                response.setToken(jwt);
                response.setExpirationTime("24Hr");
                response.setMessage("User Registered Successfully");
                response.setStatusCode(200);

                return ResponseEntity.ok(response); // Zwracamy AuthResponse przy sukcesie
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
            User user = ourUserRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            String jwt = jwtUtils.generateToken(user);
            AuthResponse response = new AuthResponse();
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");

            return ResponseEntity.ok(response); // Zwracamy AuthResponse przy sukcesie

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse(500, "Error during sign-in: Bad credentials"));
        }
    }
}