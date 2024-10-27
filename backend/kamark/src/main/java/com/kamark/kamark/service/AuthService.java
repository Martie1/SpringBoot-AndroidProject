package com.kamark.kamark.service;

import com.kamark.kamark.dto.AuthResponse;
import com.kamark.kamark.dto.LoginRequest;
import com.kamark.kamark.dto.RegisterRequest;
import com.kamark.kamark.entity.User;
import com.kamark.kamark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AuthResponse signUp(RegisterRequest registrationRequest) {
        AuthResponse response = new AuthResponse();
        try {
            // Tworzymy nowego użytkownika i ustawiamy jego dane
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

                // Ustawiamy dane odpowiedzi
               // response.setUser(ourUserResult);
                response.setToken(jwt);
                response.setExpirationTime("24Hr");  // lub inny czas ważności tokenu
                response.setMessage("User Registered Successfully");
                response.setStatusCode(200);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error during registration: " + e.getMessage());
        }
        return response;
    }

    public AuthResponse signIn(LoginRequest signinRequest) {
        AuthResponse response = new AuthResponse();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));


            User user = ourUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();


            String jwt = jwtUtils.generateToken(user);


            response.setStatusCode(200);
            response.setToken(jwt);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
          //  response.setUser(user);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error during sign-in: " + e.getMessage());
        }
        return response;
    }
}