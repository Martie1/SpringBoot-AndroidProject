package com.kamark.kamark.controller.validators;

public interface UserValidatorInterface {
    String validateUsername(String username);
    String validateEmail(String email);
    String validatePassword(String password);
    String validatePassword(String password, String username);
}

