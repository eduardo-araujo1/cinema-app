package com.eduardo.cinema_app.exceptions;

public class SeatOccupiedException extends RuntimeException{
    public SeatOccupiedException (String message) {
        super(message);
    }
}
