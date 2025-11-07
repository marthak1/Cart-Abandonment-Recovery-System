package com.shop.ecommerce_backend.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long productId;
    private BigDecimal price;
    private ProductDTO product;
    private int quantity;
    private String productName;
//    private double subtotal;



}
