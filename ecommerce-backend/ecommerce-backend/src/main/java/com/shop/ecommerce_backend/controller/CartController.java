package com.shop.ecommerce_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.service.CartServiceImpl;

import java.math.BigDecimal;
import java.time.Duration;


//TODO: HANDLE Exception
//TODO: Document API with swagger
@RestController
@RequestMapping("/api/cart")
public class CartController {


  public final CartServiceImpl cartService; // service injection for business logic

  public CartController(CartServiceImpl cartService) {  // constructor injection for better testability

      this.cartService = cartService;
  }


// 1. Create Cart
@PostMapping
public ResponseEntity<Cart> createCart(@RequestParam String sessionToken) {
    Cart cart = cartService.createCart(sessionToken);
    return ResponseEntity.status(HttpStatus.CREATED).body(cart);

}

    // Fetch Cart by Session Token
    @GetMapping("/session/{sessionToken}")
    public ResponseEntity<Cart> fetchBySessionToken(@PathVariable String sessionToken) {
        Cart cart = cartService.getCartBySessionToken(sessionToken);
//                .orElseThrow(() -> new CartNotFoundException(sessionToken));
        return ResponseEntity.ok(cart);
    }

    // Assign Cart to User Session
    @PatchMapping("/session")
    public ResponseEntity<Cart> assignCartToUserSession(@RequestHeader("X-Session-Token") String sessionToken)
    {
        Cart cart = cartService.assignCartToUserSession(sessionToken);
        return ResponseEntity.ok(cart);
    }

    // Calculate Cart Total
    @GetMapping("/{sessionToken}/total")
    public ResponseEntity<BigDecimal> calculateCartTotal(@PathVariable String sessionToken) {
        BigDecimal total = cartService.calculateCartTotal(sessionToken);
        return ResponseEntity.ok(total);
    }

    // Check if Cart is Inactive
    @GetMapping("/{sessionToken}/inactive")
    public ResponseEntity<Boolean> isCartInactive(
            @PathVariable String sessionToken,
            @RequestParam long thresholdSeconds) {
        boolean inactive = cartService.isCartInactive(sessionToken, Duration.ofSeconds(thresholdSeconds));
        return ResponseEntity.ok(inactive);
    }

    // Checkout Cart
    @PostMapping("/{sessionToken}/checkout")
    public ResponseEntity<Cart> checkoutCart(@PathVariable String sessionToken) {
        Cart cart = cartService.checkoutCart(sessionToken);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestBody Cart cart) {
        Cart clearedCart = cartService.clearCart(cart);
        return ResponseEntity.ok(clearedCart);
    }

    @DeleteMapping("/{sessionToken}")
    public ResponseEntity<Void> deleteCart(@PathVariable String sessionToken) {
        cartService.deleteCart(sessionToken);
        return ResponseEntity.noContent().build();
    }


}


