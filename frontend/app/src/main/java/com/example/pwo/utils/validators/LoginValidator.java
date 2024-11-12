package com.example.pwo.utils.validators;

import android.util.Patterns;

public class LoginValidator implements UserValidator {

    @Override
    public String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email format";
        }
        return null;
    }

    @Override
    public String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        return null;
    }

    @Override
    public String validatePassword(String password, String username) {
        return validatePassword(password);
    }

    public String validateUsername(String username){
        return null;
    }
}