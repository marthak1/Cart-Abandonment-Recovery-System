package com.shop.ecommerce_backend.service;
import com.shop.ecommerce_backend.model.Cart;
import java.math.BigDecimal;
import java.time.Duration;


public interface ICartService {
    Cart createCart(String sessionToken);
    Cart getCartBySessionToken(String sessionToken);
    Cart updateCart(Cart cart);
    Cart clearCart(Cart cart);
    BigDecimal calculateCartTotal(String sessionToken);
    Cart checkoutCart(String sessionToken);
    void deleteCart(String sessionToken);
    Cart assignCartToUserSession(String sessionToken);
    boolean isCartInactive(String sessionToken, Duration threshold);
}

// Basic Cart Logic
// The Cart represents a user's active shopping session. Its logic includes:

// Create new cart (on session start or user login)

// Fetch cart by ID or session token

// Associate cart with user or anonymous session

// Mark cart as checked out

// Calculate total price from items

// Trigger recovery modal if cart is inactive

