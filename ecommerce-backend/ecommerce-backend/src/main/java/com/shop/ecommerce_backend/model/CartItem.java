package com.shop.ecommerce_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart_item")
public class CartItem {
@Id
@GeneratedValue (strategy=GenerationType.IDENTITY)
private Long id;
@ManyToOne
@JoinColumn(name="product_id", nullable=false)

private Product product;

    private int quantity;

    private BigDecimal price;


@ManyToOne
@JoinColumn(name="cart_id", nullable=false)
private Cart cart;



//public CartItem() {}

//public CartItem(Long id, Product product, int quantity,BigDecimal price, Cart cart) {
//    this.id = id;
//    this.product = product;
//    this.quantity = quantity;
//    this.price = price;
//    this.cart = cart;
//
//}

}