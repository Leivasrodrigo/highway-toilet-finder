package com.highwaytoiletfinder.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class EmailAlreadyInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
