package com.shop.ecommerce_backend.model;


import java.math.BigDecimal;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart_item" , uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"}))
public class CartItem {
@Id
@GeneratedValue (strategy=GenerationType.IDENTITY)
private Long id;
@Column(nullable = false)
private BigDecimal price;
private int quantity;
@ManyToOne(fetch = FetchType.EAGER)
//@JsonIgnore // optional, if product causes recursion
@JoinColumn(name="product_id", nullable=false)
private Product product;
@ManyToOne
//@JsonIgnore // prevents cart → items → cart loop
@JoinColumn(name="cart_id", nullable=false)
private Cart cart;


}