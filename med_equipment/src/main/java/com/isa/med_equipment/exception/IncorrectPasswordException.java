package com.isa.med_equipment.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(final String message) {
        super(message);
    }
}