package com.kamark.kamark.controller.validators;


import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class LoginValidator implements UserValidatorInterface {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty.";
        }
        if (!pattern.matcher(email).matches()) {
            return "Invalid email format.";
        }
        return "ok";
    }

    @Override
    public String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        return "ok";
    }

    public String validateUsername(String username){
        return "ok";
    }
    @Override
    public String validatePassword(String password,String username) {
        validatePassword(password);
        return "ok";
    }
}