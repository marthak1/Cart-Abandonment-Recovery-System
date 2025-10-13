 package com.shop.ecommerce_backend.service;

 import java.util.List;
 import java.util.Optional;
 import com.shop.ecommerce_backend.model.Product;
 import com.shop.ecommerce_backend.repository.ProductRepository;
 import org.springframework.stereotype.Service;
//TODO: Handle Exception
 @Service
 public class ProductServiceImp implements IProductService {

     private final ProductRepository productRepository;

     public ProductServiceImp(ProductRepository productRepository) {

         this.productRepository = productRepository;
     }
     @Override
     public List<Product> getAllProducts(){

         return productRepository.findAll();
     }

     @Override
     public Optional<Product> getProductById(Long id) { // use optional to handle case where product id could not be found

         return productRepository.findById(id);
     }

     @Override
     public Product createProduct(Product newProduct) {

         return productRepository.save(newProduct);
     }

     @Override
     public Product updateProduct(Long id, Product updatedProduct) {
         Product existingProduct = productRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Product not found"));
//                         ProductNotFoundException(id)); // CREATE PRODUCT NOTFOUND EXCEPTION CLASS TO CLEAR ERROR

         existingProduct.setName(updatedProduct.getName());
         existingProduct.setPrice(updatedProduct.getPrice());
         existingProduct.setDescription(updatedProduct.getDescription());
         existingProduct.setImageUrl(updatedProduct.getImageUrl());

         return productRepository.save(existingProduct);
     }


    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
                    //ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

}
