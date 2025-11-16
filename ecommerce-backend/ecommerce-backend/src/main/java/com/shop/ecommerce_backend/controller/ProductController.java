package com.shop.ecommerce_backend.controller;

import java.util.List;

import com.shop.ecommerce_backend.DTO.ProductDTO;
import com.shop.ecommerce_backend.service.ProductServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shop.ecommerce_backend.model.Product;

//TODO: HANDLE EXCEPTION
//TODO: Document API with swagger
@RestController
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

  private final ProductServiceImp productService;

  public  ProductController(ProductServiceImp productService){
      this.productService=productService;
  }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
        Product product = productService.createProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProduct) {
       log.info("Updating product {} with payload {}", id, updatedProduct);
      ProductDTO updated = productService.updateProduct(id, updatedProduct);
      return ResponseEntity.ok(updated);
  }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
      productService.deleteProduct(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
