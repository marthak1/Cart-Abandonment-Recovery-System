package com.shop.ecommerce_backend.repository;

import com.shop.ecommerce_backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.ecommerce_backend.model.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

// Find cart item by cart and product ID using custom query methods
@Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product.id = :productId")
Optional<CartItem> findByCartAndProductId(
        @Param("cart") Cart cart,
        @Param("productId") Long productId
);

}
