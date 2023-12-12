package com.isa.med_equipment.exception;

public class EmailNotSentException extends RuntimeException {

    public EmailNotSentException(final String message) { super(message); }
}