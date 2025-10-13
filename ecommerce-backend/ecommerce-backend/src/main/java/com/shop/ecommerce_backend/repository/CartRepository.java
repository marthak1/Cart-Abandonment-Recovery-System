package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySessionToken(String sessionToken);
}
