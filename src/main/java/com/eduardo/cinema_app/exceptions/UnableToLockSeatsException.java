package com.eduardo.cinema_app.exceptions;

public class UnableToLockSeatsException extends RuntimeException{
    public UnableToLockSeatsException(String message){
        super(message);
    }

    public UnableToLockSeatsException(String message, Throwable cause) {
        super(message, cause);
    }
}
