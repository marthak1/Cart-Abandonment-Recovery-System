package com.shop.ecommerce_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart_item")
public class CartItem {
// Define fields, constructors, getters, and setters here
@Id
@GeneratedValue (strategy=GenerationType.IDENTITY)
private Long id;
@ManyToOne
@JoinColumn(name="product_id", nullable=false)
private Product productId; 
private int quantity;
@ManyToOne
private Cart cart;
public CartItem() {}
public CartItem(Long id, Product productId, int quantity, Cart cart) {
    this.id = id;
    this.productId = productId;
    this.quantity = quantity;
    this.cart = cart;
}
public Long getId() {
    return id;
}
public Product getProductId() {
    return productId;
}
public void setProductId(Product productId) {
    this.productId = productId;
}
public int getQuantity() {
    return quantity;}

public void setQuantity(int quantity) {
    this.quantity = quantity;}

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


}
