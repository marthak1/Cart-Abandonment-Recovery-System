package com.shop.ecommerce_backend.service;

import java.time.Duration;
import com.shop.ecommerce_backend.DTO.CartDTO;


public interface ICartService {
    CartDTO addItemToCart(String sessionToken, Long productId, Integer quantity);
    void deleteCart(String sessionToken);
    boolean isCartInactive(String sessionToken, Duration threshold);
    CartDTO fetchCart(String sessionToken);
    CartDTO removeItemFromCart(String sessionToken, Long productId);
    CartDTO updateItemQuantity(String sessionToken, Long productId, Integer quantity);
}





