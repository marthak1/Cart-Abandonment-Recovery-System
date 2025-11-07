//package com.shop.ecommerce_backend.exception;
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
