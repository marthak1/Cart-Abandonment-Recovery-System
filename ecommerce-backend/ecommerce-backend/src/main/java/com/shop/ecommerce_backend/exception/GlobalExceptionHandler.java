package com.shop.ecommerce_backend.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

//    @ExceptionHandler(InsufficientStockException.class)
//    public ResponseEntity<ErrorResponse> handleInsufficientStockException(
//            InsufficientStockException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Insufficient Stock",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        logError("InsufficientStockException", ex);
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

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
    // Session Related Exceptions
    // ========================================================================

//    @ExceptionHandler(SessionExpiredException.class)
//    public ResponseEntity<ErrorResponse> handleSessionExpiredException(
//            SessionExpiredException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.UNAUTHORIZED.value(),
//                "Session Expired",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        logError("SessionExpiredException", ex);
//        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//    }

    // ========================================================================
    // Order Related Exceptions
    // ========================================================================

//    @ExceptionHandler(OrderException.class)
//    public ResponseEntity<ErrorResponse> handleOrderException(
//            OrderException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Order Error",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        logError("OrderException", ex);
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

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
    // Request Parameter Exceptions
    // ========================================================================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Parameter",
                message,
                request.getRequestURI()
        );

        logError("MissingServletRequestParameterException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format("Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Type Mismatch",
                message,
                request.getRequestURI()
        );

        logError("MethodArgumentTypeMismatchException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ========================================================================
    // HTTP Message Not Readable
    // ========================================================================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON Request",
                "Request body is invalid or malformed. Please check your JSON syntax.",
                request.getRequestURI()
        );

        logError("HttpMessageNotReadableException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ========================================================================
    // 404 - No Handler Found
    // ========================================================================

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        String message = String.format("No handler found for %s %s",
                ex.getHttpMethod(),
                ex.getRequestURL());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint Not Found",
                message,
                request.getRequestURI()
        );

        logError("NoHandlerFoundException", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ========================================================================
    // Illegal Argument Exception
    // ========================================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Argument",
                ex.getMessage(),
                request.getRequestURI()
        );

        logError("IllegalArgumentException", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ========================================================================
    // Null Pointer Exception
    // ========================================================================

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(
            NullPointerException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Null Pointer Error",
                "An unexpected null value was encountered. Please contact support.",
                request.getRequestURI()
        );

        logError("NullPointerException", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========================================================================
    // Generic Runtime Exception
    // ========================================================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Runtime Error",
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                request.getRequestURI()
        );

        logError("RuntimeException", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
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
    @ExceptionHandler(CartRecoveryException.class)
    public ResponseEntity<ErrorResponse> handleCartRecoveryException(CartRecoveryException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Cart Recovery Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
//    @ExceptionHandler(CartRecoveryException.class)
//    public ResponseEntity<ErrorResponse> handleCartRecovery(CartRecoveryException ex) {
//        ErrorResponse error = new ErrorResponse("Cart recovery failed", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }

//    @ExceptionHandler(ProductMappingException.class)
//    public ResponseEntity<ErrorResponse> handleProductMapping(ProductMappingException ex) {
//        ErrorResponse error = new ErrorResponse("Product mapping error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
//        String message = ex.getBindingResult().getFieldErrors().stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .collect(Collectors.joining("; "));
//        ErrorResponse error = new ErrorResponse("Validation failed", message);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
//        ErrorResponse error = new ErrorResponse("Unexpected error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
}
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CartNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleCartNotFound(CartNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorResponse(ex.getMessage()));
//    }
//
//    @ExceptionHandler(ItemNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorResponse(ex.getMessage()));
//    }
//
//    @ExceptionHandler(InvalidQuantityException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidQuantity(InvalidQuantityException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse(ex.getMessage()));
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse("An unexpected error occurred."));
//    }
//}
