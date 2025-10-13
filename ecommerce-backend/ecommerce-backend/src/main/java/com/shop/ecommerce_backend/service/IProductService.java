package com.shop.ecommerce_backend.service;

import java.util.List;
import java.util.Optional;

import com.shop.ecommerce_backend.model.Product;

public interface IProductService {
List <Product> getAllProducts();
Optional<Product> getProductById(Long id);
Product createProduct(Product newProduct);
Product updateProduct(Long id, Product updatedProduct);
void deleteProduct(Long id);

}
