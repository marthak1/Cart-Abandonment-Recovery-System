package com.shop.ecommerce_backend.service;

import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.CartItem;
import java.util.List;


public interface ICartItemService {
    Cart addItemToCart(String sessionToken, CartItem newItem);
    Cart updateItemQuantity(String sessionToken, Long productId, int quantity);
    Cart removeItemFromCart(String sessionToken, Long productId);
    List<CartItem> getItems(String sessionToken);
    Cart clearItems(String sessionToken);
}

//🧱 Core Responsibilities of CartItemService
//Method	Purpose
//addItemToCart(String sessionToken, CartItem item)	Adds a new item or increments quantity if product already exists
//removeItemFromCart(String sessionToken, Long productId)	Removes a specific product from the cart
//updateItemQuantity(String sessionToken, Long productId, int quantity)	Updates quantity of a product in the cart
//getItems(String sessionToken)	Retrieves all items in the cart
//clearItems(String sessionToken)	Clears all items from the cart
