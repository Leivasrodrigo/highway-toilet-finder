package com.highwaytoiletfinder.common.exceptions;

public class ReviewNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
