package com.kamark.kamark.dtos;


public class RegisterUserDto {
    private String email;

    private String username;

    private String password;

    public String getEmail() {
        return email;
    }

    public RegisterUserDto setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getPassword() {
        return password;
    }
    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getUsername() {
        return username;
    }

    public RegisterUserDto setUsername(String username) {
        this.username= username;
        return this;
    }
    // getters and setters
}