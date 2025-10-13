package com.shop.ecommerce_backend.repository;

import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);


// Add custom query methods if needed
}
