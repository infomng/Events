package com.events.common.exception;

import com.events.common.result.EmptyResult;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public EmptyResult handleRuntime(RuntimeException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Erreur métier");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());

        return EmptyResult.failure(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public EmptyResult handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Erreur de validation");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        detail.setDetail("Les données fournies sont invalides.");
        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", Instant.now().toString());

        return EmptyResult.failure(detail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<EmptyResult> handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("Accès refusé");
        detail.setDetail("Vous n'avez pas les permissions nécessaires pour effectuer cette action.");
        detail.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(EmptyResult.failure(detail));
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public EmptyResult handleUserNotFound(UsernameNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Utilisateur introuvable");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());
        return EmptyResult.failure(detail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public EmptyResult handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Validation échouée");
        detail.setDetail("Certaines contraintes de validation ont échoué.");
        detail.setProperty("timestamp", Instant.now().toString());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String path = cv.getPropertyPath().toString();
            String message = cv.getMessage();
            errors.put(path, message);
        });

        detail.setProperty("violations", errors);
        return EmptyResult.failure(detail);
    }
}

