package com.shop.ecommerce_backend.service;

import java.util.List;

import com.shop.ecommerce_backend.DTO.ProductDTO;
import com.shop.ecommerce_backend.model.Product;

public interface IProductService {
List <ProductDTO> getAllProducts();
ProductDTO getProductById(Long id);
Product createProduct(Product newProduct);
Product updateProduct(Long id, Product updatedProduct);
void deleteProduct(Long id);

}
