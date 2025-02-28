package com.eduardo.cinema_app.exceptions;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(String message){
        super(message);
    }
}
