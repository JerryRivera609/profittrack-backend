package com.profitrack.infraestructura.configuracion;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCreds(BadCredentialsException e) {
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(Map.of("error", msg));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException e) {
        // captura de error
        int status = 500;
        String message = e.getMessage();
        if (message != null) {
            if (message.contains("No encontrado"))
                status = 404;
            else if (message.contains("No tiene permisos") || message.contains("Revocado"))
                status = 403;
            else if (message.contains("Ya existe"))
                status = 409;
        }
        return ResponseEntity.status(status).body(Map.of("error", message != null ? message : "Error interno"));
    }
}
