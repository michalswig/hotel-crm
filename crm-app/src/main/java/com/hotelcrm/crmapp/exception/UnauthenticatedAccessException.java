package com.hotelcrm.crmapp.exception;

public class UnauthenticatedAccessException extends RuntimeException {
    public UnauthenticatedAccessException(String message) {
        super(message);
    }
}
