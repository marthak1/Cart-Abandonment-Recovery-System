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
    private List<CartItem> cartItems;
    // Constructors, getters, and setters
//    public Product() {}
//    public Product(Long id, String name, String description, BigDecimal price, String imageUrl, List<CartItem> cartItems) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.imageUrl = imageUrl;
//        this.cartItems = cartItems;
//    }


}
