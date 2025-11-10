package com.shop.ecommerce_backend.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private Long productId;

    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
        this.productId = productId;
    }

    public ProductNotFoundException(String message) {
        super(message);
    }


}
