package com.example.pwo.utils.validators;

import android.util.Patterns;

public class RegisterValidator implements UserValidator {

    @Override
    public String validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty.";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters long.";
        }
        if (hasOnlyOneCharacter(username)) {
            return "Username cannot consist of one character.";
        }
        if(username.contains(";")){
            return "Username cannot contain semicolon.";
        }
        return null;
    }
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
    public String validatePassword(String password, String username) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Password must contain at least one letter.";
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password must include at least one special character (e.g., !@#$%).";
        }
        if (password.toLowerCase().contains(username.toLowerCase())) {
            return "Password cannot contain the username.";
        }
        if (hasOnlyOneCharacter(password)) {
            return "Password cannot consist of one character.";
        }
        return null;
    }

    @Override
    public String validatePassword(String password) {
        throw new UnsupportedOperationException("Use validatePassword(String password, String username) instead.");
    }
    
    public boolean hasOnlyOneCharacter(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

}
