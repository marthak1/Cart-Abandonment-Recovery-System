package com.shop.ecommerce_backend.exception;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========================================================================
    // Cart Related Exceptions
    // ========================================================================

    @ExceptionHandler(CartRecoveryException.class)
    public ResponseEntity<ErrorResponse> handleCartException(
            CartRecoveryException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Cart Error",
                ex.getMessage(),
                request.getRequestURI()
        );

        logError("CartException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCartException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCartException(
            InvalidCartException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Cart",
                ex.getMessage(),
                request.getRequestURI()
        );

        logError("InvalidCartException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ========================================================================
    // Product Related Exceptions
    // ========================================================================

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Product Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        logError("ProductNotFoundException", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    // ========================================================================
    // Resource Not Found Exception
    // ========================================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        logError("ResourceNotFoundException", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    // ========================================================================
    // Validation Exceptions
    // ========================================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Input validation failed. Check the errors field for details.",
                request.getRequestURI()
        );
        error.setValidationErrors(validationErrors);

        logError("MethodArgumentNotValidException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    // ========================================================================
    // Generic Exception (Catch-All)
    // ========================================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later or contact support.",
                request.getRequestURI()
        );

        logError("Exception", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private void logError(String exceptionType, Exception ex) {
        System.err.println("========================================");
        System.err.println(exceptionType + " occurred at " + LocalDateTime.now());
        System.err.println("Message: " + ex.getMessage());
        System.err.println("Stack trace:");
         ex.printStackTrace();
        System.err.println("========================================");
    }
}
