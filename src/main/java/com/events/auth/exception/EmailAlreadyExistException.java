package com.events.auth.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String email) {
        super("Email already exist: " + email);
    }
}
