package com.finartzIntern.HotelRezervationSys.domain.exceptions;

/**
 * Thrown when the requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
