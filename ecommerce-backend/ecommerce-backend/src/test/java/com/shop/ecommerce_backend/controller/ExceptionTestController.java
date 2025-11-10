package com.shop.ecommerce_backend.controller;

import com.shop.ecommerce_backend.exception.InvalidCartException;
import com.shop.ecommerce_backend.exception.ProductNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ExceptionTestController {

    @GetMapping("/product-not-found")
    public void testProductNotFound() {
        throw new ProductNotFoundException(999L);
    }

    @GetMapping("/generic-error")
    public void testGenericError() {
        throw new RuntimeException("Something went wrong!");
    }
    @GetMapping("/invalid-cart-error")
    public void testInvalidCartException(){
        throw new InvalidCartException("Cart is Invalid");
    }

    @GetMapping("/resource-not-found-exception")
    public void testResourceNotFoundException(){
        throw new InvalidCartException("No resource found");
    }
}
/*Test with curl:
curl http://localhost:8080/test/product-not-found
curl http://localhost:8080/test/resource-not-found-exception
        */