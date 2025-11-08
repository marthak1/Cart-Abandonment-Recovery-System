package com.shop.ecommerce_backend.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDTO {
    private Long id;
    private String sessionToken;
    private List<CartItemDTO> items;
    private BigDecimal total;
    LocalDateTime createdAt;
    LocalDateTime lastUpdated;
//    private String status;
    private boolean recoveryFlag;
}

