package com.shop.ecommerce_backend.controller;

import java.util.List;
import com.shop.ecommerce_backend.service.ProductServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shop.ecommerce_backend.model.Product;

//TODO: HANDLE EXCEPTION
//TODO: Document API with swagger
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductServiceImp productService;

  public  ProductController(ProductServiceImp productService){
      this.productService=productService;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
          List <Product> allProducts = productService.getAllProducts();
      return ResponseEntity.status(HttpStatus.OK)
                           .body((allProducts));
  }


  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable Long id) {
      Product product = productService.getProductById(id)
              .orElseThrow(() -> new RuntimeException("product not found"));
                      //ProductNotFoundException(id)); -> USE THIS FOR GLOBAL EXCEPTION
      return ResponseEntity.ok(product);
  }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
       Product product = productService.createProduct(newProduct);
       return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }
  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
      Product product = productService.updateProduct(id, updatedProduct);
      return ResponseEntity.ok(product);
  }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
      productService.deleteProduct(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
