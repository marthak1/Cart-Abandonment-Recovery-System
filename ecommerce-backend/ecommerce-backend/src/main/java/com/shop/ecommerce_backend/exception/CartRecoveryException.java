package com.shop.ecommerce_backend.exception;

public class CartRecoveryException extends RuntimeException {
    public CartRecoveryException(String message) {
        super(message);
    }

    public CartRecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}