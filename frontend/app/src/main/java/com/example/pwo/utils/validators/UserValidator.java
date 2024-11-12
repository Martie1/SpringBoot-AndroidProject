package com.example.pwo.utils.validators;

public interface UserValidator {
    String validateUsername(String username);
    String validateEmail(String email);
    String validatePassword(String password);
    String validatePassword(String password, String username);
}

