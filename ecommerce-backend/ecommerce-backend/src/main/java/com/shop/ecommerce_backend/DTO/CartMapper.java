package com.shop.ecommerce_backend.DTO;

import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.model.Product;
import org.springframework.stereotype.Component;
// import java.time.Duration;
// import java.time.LocalDateTime;
import java.util.stream.Collectors;
@Component
public class CartMapper {
    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setSessionToken(cart.getSessionToken());
        dto.setStatus(String.valueOf(cart.getStatus()));
        dto.setRecoveryFlag(cart.getRecoveryFlag()); // Include recovery flag
        dto.setLastUpdated(cart.getLastUpdated());
        dto.setAbandonedAt(cart.getAbandonedAt());

        if (cart.getItems() != null) {
            dto.setItems(
                    cart.getItems().stream()
                            .map(this::toItemDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

//    public boolean isCartInactive(LocalDateTime lastUpdated, Duration threshold) {
//        return lastUpdated != null && Duration.between(lastUpdated, LocalDateTime.now()).compareTo(threshold) > 0;
//    }


    // Map CartItem to CartItemDTO with Product details
    public CartItemDTO toItemDTO(CartItem item) {
        if (item == null) {
            return null;
        }

        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setProductName(item.getProduct().getName());
//        dto.setSubtotal(item.getQuantity() * item.getProduct().getPrice()); // fix


        // Map product details
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProduct(toProductDTO(item.getProduct()));
        }

        return dto;
    }

    // Map Product to ProductDTO
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }
}








