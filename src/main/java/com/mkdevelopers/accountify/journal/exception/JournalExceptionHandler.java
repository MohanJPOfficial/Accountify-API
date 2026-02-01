package com.mkdevelopers.accountify.journal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class JournalExceptionHandler {

    @ExceptionHandler(JournalNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(JournalNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateJournalException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateJournalException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
