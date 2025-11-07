package com.shop.ecommerce_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "session_token", nullable = false, unique = true)
    private String sessionToken;
    @Column(name = "recovered", nullable = false)
    private boolean recovered = false;
    @Column(name = "is_checked_out", nullable = false)
    private boolean isCheckedOut;
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    private String status;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // one-to-many relationship with CartItem
//   @JsonIgnore  // optional, if am not returning cart with items yet
    private List<CartItem> items = new ArrayList<>();


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

