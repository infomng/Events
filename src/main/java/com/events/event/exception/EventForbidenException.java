package com.events.event.exception;

import com.events.auth.exception.ForbidenException;

public class EventForbidenException extends ForbidenException {

    public EventForbidenException(Long id) {
        super("You cannot access the event with id: " + id );
    }
}
