package com.highwaytoiletfinder.common.exceptions;

public class ToiletNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ToiletNotFoundException(String message) {
        super(message);
    }
}
