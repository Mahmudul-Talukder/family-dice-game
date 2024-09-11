package io.hishab.familygame.exception;

import io.hishab.familygame.model.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     *
     * @param ex the validation exception
     * @return a response entity containing validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom game exceptions.
     *
     * @param ex the game exception
     * @return a response entity containing the error details
     */
    @ExceptionHandler(GameException.class)
    public ResponseEntity<CustomErrorResponse> handleGameException(GameException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getErrorCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles constraint violation exceptions.
     *
     * @param ex the constraint violation exception
     * @return a response entity containing the error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "ERR-400",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HTTP client error exceptions.
     *
     * @param ex the HTTP client error exception
     * @return a response entity containing the error details
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                ex.getStatusCode().value(),
                "ERR-4XX",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    /**
     * Handles HTTP server error exceptions.
     *
     * @param ex the HTTP server error exception
     * @return a response entity containing the error details
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpServerErrorException(HttpServerErrorException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                ex.getStatusCode().value(),
                "ERR-5XX",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    /**
     * Handles generic exceptions.
     *
     * @param ex the generic exception
     * @return a response entity containing the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "ERR-500",
                "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


