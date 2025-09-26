package com.shop.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.ecommerce_backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
