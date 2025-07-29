package com.events.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbidenException extends RuntimeException {

    public ForbidenException(String message) {
        super(message);
    }
}
