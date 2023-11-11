package com.isa.med_equipment.validation;

public class EmailExistsException extends Exception {

    public EmailExistsException(final String message) {
        super(message);
    }
}
