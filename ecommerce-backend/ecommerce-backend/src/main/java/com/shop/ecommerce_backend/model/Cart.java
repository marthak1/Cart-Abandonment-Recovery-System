package com.shop.ecommerce_backend.model;

import java.time.LocalDateTime;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionToken;

    private boolean isCheckedOut;
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL) // one-to-many relationship with CartItem
    private List<CartItem> items;

    public Cart(String sessionToken) {
        this.sessionToken = sessionToken;
    }

//    public Cart() {
//
//    }
//    public Cart(Long id, boolean isCheckedOut, List<CartItem> items, LocalDateTime lastUpdated, String sessionToken) {
//        this.id = id;
//        this.isCheckedOut = isCheckedOut;
//        this.items = items;
//        this.lastUpdated = lastUpdated;
//        this.sessionToken = sessionToken;
//    }
//public Cart(String sessionToken) {
//    this.sessionToken = sessionToken;
//    this.items = new ArrayList<>();
//    this.isCheckedOut = false;
//    this.lastUpdated = LocalDateTime.now();
//
//}

}
//KeyNotes:
// - **One Cart** can have **many CartItems**.
// - The relationship is managed by the `cart` field in `CartItem`.
// - All operations on `Cart` are cascaded to its `CartItems`.

//sessionToken is the anchor for cart recovery
//
//lastUpdated supports inactivity detection
//
//isCheckedOut prevents further edits post-purchase