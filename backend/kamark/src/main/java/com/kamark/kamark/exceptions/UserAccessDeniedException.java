package com.kamark.kamark.exceptions;

public class UserAccessDeniedException extends RuntimeException{
    public  UserAccessDeniedException(String message) {
        super(message);
    }
}
