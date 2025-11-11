package com.shop.ecommerce_backend.model;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;
    private BigDecimal price;
    private String imageUrl;

    @OneToMany(mappedBy = "product")
//   @JsonIgnore // prevents cartItems from being serialized, (used for now : considering using of DTO)
    private List<CartItem> cartItems;



}
