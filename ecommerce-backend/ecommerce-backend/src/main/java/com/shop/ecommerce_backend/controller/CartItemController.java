////package com.shop.ecommerce_backend.controller;
////
////import com.shop.ecommerce_backend.DTO.CartMapper;
////import com.shop.ecommerce_backend.service.CartServiceImpl;
////import lombok.AllArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import com.shop.ecommerce_backend.model.Cart;
////import io.swagger.v3.oas.annotations.Operation;
////import io.swagger.v3.oas.annotations.responses.ApiResponse;
////import io.swagger.v3.oas.annotations.responses.ApiResponses;
////import io.swagger.v3.oas.annotations.tags.Tag;
////
////
//
////@RestController
////@Slf4j
////@AllArgsConstructor
////@RequestMapping("/api/cartItems") // Base path
////@Tag(name = "Cart Item Operations", description = "Manage items within a cart using session token")
////public class CartItemController {
////
////    private final CartServiceImpl cartService;
////    private final CartMapper cartMapper;
//
//
////    @Operation(summary = "Add item to cart")
////    @ApiResponses({
////            @ApiResponse(responseCode = "200", description = "Item added successfully"),
////            @ApiResponse(responseCode = "404", description = "Cart or product not found")
////    })
////

////
////

//
//
//
//
//
//
//
//    //    @PutMapping("/{sessionToken}/updateItem/{id}")
////    public ResponseEntity<CartItemDTO> updateItemQuantity(
////
////            @PathVariable String sessionToken,
////            @PathVariable Long id,
////            @RequestParam int quantity) {
////        log.info("Updating item: session={}, id={}, quantity={}", sessionToken, id, quantity);
////        CartItemDTO updatedCart = cartItemService.updateItemQuantity(sessionToken, id, quantity);
////        return ResponseEntity.ok(updatedCart);
////
////
////
////    }
//
//
//
//    //    @PutMapping("/updateQuantity")
////    public ResponseEntity<CartDTO> updateQuantity(
////            @RequestHeader("X-Session-Token") String sessionToken,
////            @RequestBody Map<String, Object> request) {
////
////        Long productId = ((Number) request.get("productId")).longValue();
////        Integer quantity = (Integer) request.get("quantity");
////
////        CartDTO updatedCart = cartService.updateItemQuantity(sessionToken, productId, quantity);
////        return ResponseEntity.ok(updatedCart);
////    }
////    @GetMapping("/{sessionToken}/items")
////    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String sessionToken) {
////        List<CartItem> items = cartItemService.getItemsBySessionToken(sessionToken);
////        if (items.isEmpty()) {
////            return ResponseEntity.noContent().build(); // or notFound()
////        }
////        return ResponseEntity.ok(items);
////    }
////                @PutMapping("/items/{id}")
////            public ResponseEntity<CartItemDTO> updateItem(
////                    @RequestHeader("X-Session-Token") String sessionToken,
////                    @PathVariable Long cartItemId,
////                    @RequestBody Map<String, Integer> request) {
////                Integer quantity = request.get("quantity");
////                CartItemDTO item = cartItemService.updateItemQuantity(sessionToken, cartItemId, quantity);
////                return ResponseEntity.ok(item);
////            }
////@PutMapping("/items/{id}")
////public ResponseEntity<CartDTO> updateItem(
////        @RequestHeader("X-Session-Token") String sessionToken,
////        @PathVariable Long cartItemId,
////        @RequestBody Map<String, Integer> request) {
////
////    Integer quantity = request.get("quantity");
////    Cart updatedCart = cartItemService.updateItemQuantity(sessionToken, cartItemId, quantity);
////    CartDTO cartDTO = convertToDTO(updatedCart); // ✅ hydrate full cart
////    return ResponseEntity.ok(cartDTO);
////}
//
//
//
//}
//
////    @PostMapping("/{sessionToken}/items")
////    public ResponseEntity<Cart> addItemToCart(
////            @PathVariable String sessionToken,
////            @RequestBody CartItem newItem) {  //expects a cart item in body
////        Cart updatedCart = cartItemService.addItemToCart(sessionToken, newItem); // returns a full cart object
////        return ResponseEntity.ok(updatedCart);
////    }
////
////     @PostMapping
////     public ResponseEntity<CartItemDTO> createCartItem(
////             @RequestBody CartItemDTO cartItemDTO,
////             @RequestParam String sessionToken
////     ) {
////         CartItemDTO savedItem = cartItemService.createCartItem(cartItemDTO, sessionToken);
////         return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
////     }
//
////    @PostMapping
////            public ResponseEntity<CartItemDTO> addItemToCart(
////                    @RequestHeader("X-Session-Token") String sessionToken,
////                    @RequestBody Map<String, Object> request) {
////                Long productId = ((Number) request.get("productId")).longValue();
////                Integer quantity = (Integer) request.get("quantity");
////
////                CartItemDTO item = cartItemService.addItemToCart(sessionToken, productId, quantity);
////                return ResponseEntity.ok(item);
////            }
//
////    @GetMapping("/{sessionToken}/items")
////    public ResponseEntity<List<CartItem>> getItems(@PathVariable String sessionToken) {
////        List<CartItem> items = cartItemService.getItems(sessionToken);
////        return ResponseEntity.ok(items);
////    }
////@GetMapping("/{sessionToken}/items")
////public ResponseEntity<List<CartItem>> getItems(@PathVariable String token) {
////    List<CartItem> items = cartItemService.getItemsBySessionToken(token);
////    if (items == null) {
////        return ResponseEntity.notFound().build();
////    }
////    return ResponseEntity.ok(items);
////}
//
////        private SessionService sessionService;
////
////        @PostMapping
////        public ResponseEntity<SessionDTO> createSession() {
////            SessionDTO session = sessionService.createSession();
////            return ResponseEntity.ok(session);
////        }
////
////        @GetMapping("/validate")
////        public ResponseEntity<Map<String, Boolean>> validateSession(
////                @RequestHeader("X-Session-Token") String sessionToken) {
////            boolean valid = sessionService.isValidSession(sessionToken);
////            return ResponseEntity.ok(Map.of("valid", valid));
////        }
////
////        @PutMapping("/activity")
////        public ResponseEntity<Map<String, Boolean>> updateActivity(
////                @RequestHeader("X-Session-Token") String sessionToken,
////                @RequestBody Map<String, Long> request) {
////            sessionService.updateLastActivity(sessionToken, request.get("timestamp"));
////            return ResponseEntity.ok(Map.of("success", true));
////        }
////
////        @GetMapping
////        public ResponseEntity<SessionDTO> getSession(
////                @RequestHeader("X-Session-Token") String sessionToken) {
////            SessionDTO session = sessionService.getSession(sessionToken);
////            return ResponseEntity.ok(session);
////        }
////}
//
//// File: src/main/java/com/cartrecovery/controller/CartController.java
//
////        package com.cartrecovery.controller;
////
////import com.cartrecovery.model.dto.CartDTO;
////import com.cartrecovery.model.dto.CartItemDTO;
////import com.cartrecovery.service.CartService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////        import java.util.Map;
////
////        @RestController
////        @RequestMapping("/api/cart")
////        @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
////        public class CartController {
////
////            @Autowired
////            private CartService cartService;
////
////            @GetMapping
////            public ResponseEntity<CartDTO> getCart(
////                    @RequestHeader("X-Session-Token") String sessionToken) {
////                CartDTO cart = cartService.getCartBySession(sessionToken);
////                return ResponseEntity.ok(cart);
////            }
////
////            @PostMapping("/items")
////            public ResponseEntity<CartItemDTO> addItem(
////                    @RequestHeader("X-Session-Token") String sessionToken,
////                    @RequestBody Map<String, Object> request) {
////                Long productId = ((Number) request.get("productId")).longValue();
////                Integer quantity = (Integer) request.get("quantity");
////
////                CartItemDTO item = cartService.addItem(sessionToken, productId, quantity);
////                return ResponseEntity.ok(item);
////            }
////
////            @PutMapping("/items/{cartItemId}")
////            public ResponseEntity<CartItemDTO> updateItem(
////                    @RequestHeader("X-Session-Token") String sessionToken,
////                    @PathVariable Long cartItemId,
////                    @RequestBody Map<String, Integer> request) {
////                Integer quantity = request.get("quantity");
////                CartItemDTO item = cartService.updateQuantity(sessionToken, cartItemId, quantity);
////                return ResponseEntity.ok(item);
////            }
////
////            @DeleteMapping("/items/{cartItemId}")
////            public ResponseEntity<Void> removeItem(
////                    @RequestHeader("X-Session-Token") String sessionToken,
////                    @PathVariable Long cartItemId) {
////                cartService.removeItem(sessionToken, cartItemId);
////                return ResponseEntity.noContent().build();
////            }
////
////            @DeleteMapping
////            public ResponseEntity<Void> clearCart(
////                    @RequestHeader("X-Session-Token") String sessionToken) {
////                cartService.clearCart(sessionToken);
////                return ResponseEntity.noContent().build();
////            }
////        }
//Update Item Quantity
//    @Operation(summary = "Update quantity of a cart item by product ID")
//    @PutMapping("/{sessionToken}/item/{productId}")
////    public ResponseEntity<CartDTO> updateItemQuantity(
////            @PathVariable String sessionToken,
////            @PathVariable Long productId,
////            @RequestBody Map<String, Integer> request) {
////
////        Integer quantity = request.get("quantity");
////        CartDTO updatedCart = cartService.updateItemQuantity(sessionToken, productId, quantity);
////        return ResponseEntity.ok(updatedCart); // ✅ returns hydrated DTO with total
////    }
//
////    @PutMapping ("/{sessionToken}/item/{productId}")
//////            ("/{sessionToken}/updateItem/{id}")
////    public ResponseEntity<CartDTO> updateItemQuantity(
////            @RequestHeader("X-Session-Token") String sessionToken,
////            @PathVariable Long cartItemId,
////            @RequestBody Map<String, Integer> request) {
////
////        Integer quantity = request.get("quantity");
////        Cart updatedCart = cartService.updateItemQuantity(sessionToken, cartItemId, quantity);
////        return ResponseEntity.ok(cartMapper.toDTO(updatedCart));// ✅ hydrate full cart
////
////    }
//    @Operation(summary = "Remove an item from cart")
//    @DeleteMapping("/{sessionToken}/item/{id}")
//    public ResponseEntity<CartDTO> removeItemFromCart(
//            @PathVariable String sessionToken,
//            @PathVariable Long id) {
//
//        Cart cart = cartService.removeItemFromCart(sessionToken, id);
//        CartDTO dto = cartMapper.toDTO(cart);
//        dto.setTotal(cartService.calculateTotal(cart)); // ✅ inject total
//        return ResponseEntity.ok(dto);
//    }
//    // Clear All Items In Cart
//    @DeleteMapping("/{sessionToken}/clearCart")
//    public ResponseEntity<CartDTO> clearCart(@PathVariable String sessionToken) {
//        Cart cleared = cartService.clearCart(sessionToken);
//        return ResponseEntity.ok(cartMapper.toDTO(cleared));
//    }
//    // Calculate Cart Total
////    @GetMapping("/{sessionToken}/total")
////    public ResponseEntity<Map<String, BigDecimal>> getCartTotal(@PathVariable String sessionToken) {
////        try {
////            BigDecimal total = cartService.calculateCartTotal(sessionToken);
////            return ResponseEntity.ok(Map.of("total", total));
////        } catch (RuntimeException e) {
////            // Optional: log error for examiner traceability
////            System.err.println("Error calculating cart total: " + e.getMessage());
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("total", BigDecimal.ZERO));
////        }
//    }
//
//    // Check if Cart is Inactive
//    @GetMapping("/{sessionToken}/inactive")
//    public ResponseEntity<Boolean> isCartInactive(
//            @PathVariable String sessionToken,
//            @RequestParam long thresholdSeconds) {
//        boolean inactive = cartService.isCartInactive(sessionToken, Duration.ofSeconds(thresholdSeconds));
//        return ResponseEntity.ok(inactive);
//    }
//
//    // Complete delete of Cart
//    @DeleteMapping("/{sessionToken}")
//    public ResponseEntity<Void> deleteCart(@PathVariable String sessionToken) {
//        cartService.deleteCart(sessionToken);
//        return ResponseEntity.noContent().build();
//    }










//    @Operation(summary = "Remove an item from cart")
//    @DeleteMapping("/{sessionToken}/deleteItem/{id}")
//    public ResponseEntity<Cart> removeItemFromCart(
//            @PathVariable String sessionToken,
//            @PathVariable Long id) {
//        Cart itemRemoved = cartService.removeItemFromCart(sessionToken, id);
//        return ResponseEntity.ok(itemRemoved);
//    }
// @Operation(summary = "Remove item from cart")
//    @DeleteMapping("/{sessionToken}/clearCartItems")
//    public ResponseEntity<Cart> clearCart(@PathVariable String sessionToken) {
//        Cart clearedCart = cartService.clearCart(sessionToken);
//        return ResponseEntity.ok(clearedCart);
//    }

// 1. Create Cart
//@PostMapping
//public ResponseEntity<CartDTO> createCart(@RequestParam String sessionToken) { //@RequestParam to get session token from query param: // sessionToken is expected as ?sessionToken=abc123
//    CartDTO cart = cartService.createCartIfMissing(sessionToken);
//    return ResponseEntity.status(HttpStatus.CREATED).body(cart);
//
//}
//    public ResponseEntity<BigDecimal> calculateCartTotal(@PathVariable String sessionToken) {
//        BigDecimal total = cartService.calculateCartTotal(sessionToken);
//        return ResponseEntity.ok(Map.of("total", total));
//
//    }
// Checkout Cart
//    @PostMapping("/{sessionToken}/checkout")
//    public ResponseEntity<Cart> checkoutCart(@PathVariable String sessionToken) {
//        Cart cart = cartService.checkoutCart(sessionToken);
//        return ResponseEntity.ok(cart);
//    }

//     @PutMapping("/update") //removed update cart endpoint Redundant if item-level mutations already update cart state
//     public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {
//         Cart updatedCart = cartService.updateCart(cart);
//         return ResponseEntity.ok(updatedCart);
//     }
//    @PatchMapping("/updateCart")
//    public CartDTO updateCart(@RequestBody CartDTO incomingCart) {
//        Cart cart = cartService.findBySessionToken(incomingCart.getSessionToken())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        // Replace items, update metadata
//        cart.setItems(mapToEntities(incomingCart.getItems()));
//        cart.setLastUpdated(LocalDateTime.now());
//
//        cartService.save(cart);
//
//        // Recalculate total
//        BigDecimal total = cart.getItems().stream()
//                .filter(i -> i.getPrice() != null)
//                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        CartDTO dto = convertToDTO(cart);
//        dto.setTotal(total);
//        return dto;
//    }

//    @PutMapping("/updateQuantity")
//    public ResponseEntity<CartDTO> updateQuantity(
//            @RequestHeader("X-Session-Token") String sessionToken,
//            @RequestBody Map<String, Object> request) {
//
//        Long productId = ((Number) request.get("productId")).longValue();
//        Integer quantity = (Integer) request.get("quantity");
//
//        CartDTO updatedCart = cartService.updateItemQuantity(sessionToken, productId, quantity);
//        return ResponseEntity.ok(updatedCart);
//    }
//    @GetMapping
//    public ResponseEntity<CartDTO> fetchCart(@RequestHeader("X-Session-Token") String sessionToken) {
//        CartDTO cart = cartService.getCartBySessionToken(sessionToken);
////                .orElseThrow(() -> new CartNotFoundException(sessionToken));
//        return ResponseEntity.ok(cart);
//    }