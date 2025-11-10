package com.shop.ecommerce_backend.controller;
import java.util.Map;
import com.shop.ecommerce_backend.DTO.CartDTO;
import com.shop.ecommerce_backend.DTO.CartMapper;
import com.shop.ecommerce_backend.exception.InvalidCartException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.shop.ecommerce_backend.service.CartServiceImpl;


//TODO: HANDLE Exception Globally
//TODO: Document API with swagger
    //TODO:ADD OTHER ENDPOINTS- REMOVE ITEM, DELETE .....
@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
@Validated
@Tag(name = "Cart Item Operations", description = "Manage items within a cart using session token")
public class CartController {
    private final CartServiceImpl cartService;
    private final CartMapper cartMapper;


    @Operation(summary = "Add item to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item added successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })


    // Add Item to cart
        @PostMapping("/addItem")
    public ResponseEntity<CartDTO> addItem(
            @Valid @RequestBody Map<String, Object> request,
            @RequestHeader("X-Session-Token") String sessionToken) {

        Long productId = ((Number) request.get("productId")).longValue();
        Integer quantity = (Integer) request.getOrDefault("quantity", 1);
// Additional validation
        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new InvalidCartException("Session token is required");
        }
        System.out.println("Adding item - Session: " + sessionToken + ", Product: " + productId);

        // Add item and get complete updated cart
        CartDTO cart = cartService.addItemToCart(sessionToken, productId, quantity);

        System.out.println("Cart after add: " + cart.getItems().size() + " items, Total: " + cart.getTotal());

        // return complete CartDTO with all items
        return ResponseEntity.ok(cart);
    }

        // Fetch Cart by Session Token
    @Operation(summary = "Hydrate cart by session token")
    @GetMapping("/{sessionToken}")
    public ResponseEntity<CartDTO> fetchCart(@PathVariable String sessionToken) {
        CartDTO hydratedCart = cartService.fetchCart(sessionToken); // match update flow
        return ResponseEntity.ok(hydratedCart);
    }

    @Operation(summary = "Update quantity of a cart item by product ID")
    @PutMapping("/{sessionToken}/item/{productId}")
    public ResponseEntity<CartDTO> updateItemQuantity(
            @PathVariable String sessionToken,
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> request,
            @RequestHeader(value = "X-Session-Token", required = false) String headerToken) {

        Integer quantity = request.get("quantity");

        if (quantity == null) {
            throw new InvalidCartException("Quantity is required");
        }

        CartDTO updatedCart = cartService.updateItemQuantity(sessionToken, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

//REMOVE item by PRODUCT ID
@DeleteMapping("/{sessionToken}/item/{productId}")
public ResponseEntity<CartDTO> removeItem(
        @PathVariable String sessionToken,
        @PathVariable Long productId,
        @RequestHeader(value = "X-Session-Token", required = false) String headerToken) {

    System.out.println("Removing product: " + productId + " from session: " + sessionToken);

    // return updated CartDTO
    CartDTO updatedCart = cartService.removeItemFromCart(sessionToken, productId);

    System.out.println("Cart after removal: " + updatedCart.getItems().size() + " items");

    return ResponseEntity.ok(updatedCart);
}
    @Operation(summary = "Clear all cart")
    @DeleteMapping("/{sessionToken}/clearCartItems")
    public ResponseEntity<CartDTO> clearCartData(@PathVariable String sessionToken) {
        CartDTO clearedCart = cartService.clearCart(sessionToken);
        return ResponseEntity.ok(clearedCart);
    }
    // Check if cart should show recovery modal
    @GetMapping("/{sessionToken}/recovery-status")
    public ResponseEntity<Map<String, Boolean>> getRecoveryStatus(
            @PathVariable String sessionToken,
            @RequestHeader("X-Session-Token") String headerToken) {

        CartDTO cart = cartService.fetchCart(sessionToken);
        boolean shouldShowModal = cart.getRecoveryFlag() != null && cart.getRecoveryFlag();

        return ResponseEntity.ok(Map.of(
                "showRecoveryModal", shouldShowModal,
                "hasItems", !cart.getItems().isEmpty()
        ));
    }

    // Reset recovery flag when user dismisses modal
    @PostMapping("/{sessionToken}/dismiss-recovery")
    public ResponseEntity<Map<String, String>> dismissRecovery(
            @PathVariable String sessionToken,
            @RequestHeader("X-Session-Token") String headerToken) {

        cartService.resetRecoveryFlag(sessionToken);
        return ResponseEntity.ok(Map.of("message", "Recovery dismissed"));
    }

    // Mark cart as recovered when user responds to modal
    @PostMapping("/{sessionToken}/mark-recovered")
    public ResponseEntity<Map<String, String>> markRecovered(
            @PathVariable String sessionToken,
            @RequestHeader("X-Session-Token") String headerToken) {

        cartService.markCartAsRecovered(sessionToken);
        return ResponseEntity.ok(Map.of("message", "Cart marked as recovered"));
    }

    // Update activity timestamp
    @PostMapping("/{sessionToken}/activity")
    public ResponseEntity<Map<String, String>> updateActivity(
            @PathVariable String sessionToken,
            @RequestHeader("X-Session-Token") String headerToken) {

        cartService.updateCartActivity(sessionToken);
        return ResponseEntity.ok(Map.of("message", "Activity updated"));
    }

}

//    @DeleteMapping("/{sessionToken}/deleteItem/{id}")
//    public ResponseEntity<CartDTO> removeItemFromCart(
//            @PathVariable String sessionToken,
//            @PathVariable Long id) {
//        CartDTO itemRemoved = cartService.removeItemFromCart(sessionToken, id);
//        return ResponseEntity.ok(itemRemoved);
//    }


