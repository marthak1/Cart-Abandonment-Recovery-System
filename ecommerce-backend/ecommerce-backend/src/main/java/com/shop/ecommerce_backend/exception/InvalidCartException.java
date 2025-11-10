package com.shop.ecommerce_backend.exception;

public class InvalidCartException extends RuntimeException {
    public InvalidCartException(String message) {
        super(message);
    }
}