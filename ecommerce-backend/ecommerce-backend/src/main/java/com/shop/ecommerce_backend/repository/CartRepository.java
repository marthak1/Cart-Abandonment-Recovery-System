package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.Cart;



public interface CartRepository extends JpaRepository<Cart, Long> {

}
