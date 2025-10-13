package com.shop.ecommerce_backend.controller;

import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.service.CartItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.junit.jupiter.api.Tag;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//TODO: Document API with swagger
@RestController
@RequestMapping("/api/items")
@Tag(name = "Cart Item Operations", description = "Manage items within a cart using session token")
public class CartItemController {


    private final CartItemServiceImpl cartItemService;

    public CartItemController(CartItemServiceImpl cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "Add item to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item added successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @PostMapping("/{sessionToken}/items")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable String sessionToken,
            @RequestBody CartItem newItem) {
        Cart updatedCart = cartItemService.addItemToCart(sessionToken, newItem);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{sessionToken}/items/{productId}")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable String sessionToken,
            @PathVariable Long productId) {
        Cart updatedCart = cartItemService.removeItemFromCart(sessionToken, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @PatchMapping("/{sessionToken}/items/{productId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable String sessionToken,
            @PathVariable Long productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartItemService.updateItemQuantity(sessionToken, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/{sessionToken}/items")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable String sessionToken) {
        List<CartItem> items = cartItemService.getItems(sessionToken);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{sessionToken}/items")
    public ResponseEntity<Cart> clearItems(@PathVariable String sessionToken) {
        Cart clearedCart = cartItemService.clearItems(sessionToken);
        return ResponseEntity.ok(clearedCart);
    }

}
