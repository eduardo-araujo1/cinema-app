package com.eduardo.cinema_app.exceptions;

public class SessionAlreadyExistsException extends RuntimeException{
    public SessionAlreadyExistsException(String message) {
        super(message);
    }
}
