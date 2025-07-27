package com.events.common.exception;

import com.events.common.result.EmptyResult;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<EmptyResult> handleRuntime(RuntimeException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(ex.getClass().getSimpleName());
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());
        detail.setProperty("stackTrace", ex.getStackTrace()[0].toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(EmptyResult.failure(detail));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<EmptyResult> handleWebClientResponseException(org.springframework.web.reactive.function.client.WebClientResponseException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(ex.getStatusCode());
        detail.setTitle("Erreur WebClient");
        detail.setDetail(ex.getMessage() + " " + ex.getResponseBodyAsString());
        detail.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(EmptyResult.failure(detail));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmptyResult> handleInternalServerError(Exception ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle("Erreur interne du serveur");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(EmptyResult.failure(detail));
    }

  /* @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
   public ResponseEntity<String> handleExpiredJwtException(io.jsonwebtoken.ExpiredJwtException ex) {
       return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
               .body("Le jeton JWT a expiré. Veuillez vous reconnecter.");
   }*/

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<EmptyResult> handleJwtException(JwtException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle("Erreur JWT");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(EmptyResult.failure(detail));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmptyResult> handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Erreur de validation");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        detail.setDetail("Les données fournies sont invalides.");
        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", Instant.now().toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(EmptyResult.failure(detail));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<EmptyResult> handleUserNotFound(UsernameNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("Utilisateur introuvable");
        detail.setDetail(ex.getMessage());
        detail.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(EmptyResult.failure(detail));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<EmptyResult> handleConstraintViolation(ConstraintViolationException ex) {
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
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(EmptyResult.failure(detail));
    }
}

