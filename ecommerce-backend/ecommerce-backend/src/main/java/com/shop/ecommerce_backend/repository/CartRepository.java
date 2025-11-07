package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Use JOIN FETCH to eagerly load items and products
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.sessionToken = :sessionToken")
    Optional<Cart> findBySessionToken(@Param("sessionToken") String sessionToken);
//    Optional<Cart> findBySessionToken(String sessionToken);
}
