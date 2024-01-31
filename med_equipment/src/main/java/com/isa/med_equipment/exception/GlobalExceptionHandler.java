package com.isa.med_equipment.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The data has been modified by another user. Please refresh and try again.");
    }

    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ResponseEntity<String> handlePessimisticLLockingFailureException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The data has been modified by another user. Please refresh and try again.");
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, ClassCastException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({EmailNotSentException.class, QRCodeGenerationException.class})
    public ResponseEntity<String> handleInternalExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
