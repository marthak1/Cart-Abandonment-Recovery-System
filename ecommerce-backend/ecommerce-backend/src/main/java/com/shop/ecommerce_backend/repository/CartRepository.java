package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Use JOIN FETCH to eagerly load items and products
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.sessionToken = :sessionToken")
    Optional<Cart> findBySessionToken(@Param("sessionToken") String sessionToken);

//    @Query("SELECT c FROM Cart c WHERE c.status = 'ACTIVE' AND c.lastUpdated < :threshold AND c.checkedOut = false")
//    List<Cart> findAbandonedCarts(LocalDateTime threshold);
    List<Cart> findByStatus(Cart.CartStatus status);

//    Optional<Cart> findBySessionToken(String sessionToken);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.sessionToken = :sessionToken")
    Optional<Cart> findBySessionTokenWithItems(String sessionToken);

    // Find carts that haven't been updated in X time
    @Query("SELECT c FROM Cart c WHERE c.status = :status AND c.lastUpdated < :cutoff")
    List<Cart> findInactiveCarts(Cart.CartStatus status, LocalDateTime cutoff);
}
