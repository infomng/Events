package com.events.auth.exception;

public class UserNotVerifiedException extends RuntimeException {

    public UserNotVerifiedException(String message) {
        super(message);
    }
}
