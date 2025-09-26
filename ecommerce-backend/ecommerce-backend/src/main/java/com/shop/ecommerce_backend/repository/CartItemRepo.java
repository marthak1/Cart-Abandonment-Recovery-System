package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.CartItem;
public interface CartItemRepo extends JpaRepository<CartItem, Long> {

}
