package com.events.modules.event.exception;

import com.events.modules.auth.exception.ForbidenException;

public class EventForbidenException extends ForbidenException {

    public EventForbidenException(Long id) {
        super("You cannot access the event with id: " + id );
    }
}
