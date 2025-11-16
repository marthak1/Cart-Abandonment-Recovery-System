 package com.shop.ecommerce_backend.service;

 import java.util.List;
 import java.util.stream.Collectors;

 import com.shop.ecommerce_backend.DTO.CartMapper;
 import com.shop.ecommerce_backend.exception.ProductNotFoundException;
 import lombok.AllArgsConstructor;
 import org.springframework.stereotype.Service;
 import com.shop.ecommerce_backend.DTO.ProductDTO;
 import com.shop.ecommerce_backend.model.Product;
 import com.shop.ecommerce_backend.repository.ProductRepository;
//TODO: Handle Exception
 @Service
 @AllArgsConstructor
 public class ProductServiceImp implements IProductService {
     private final ProductRepository productRepository;
     private final CartMapper cartMapper;
//     public ProductServiceImp(ProductRepository productRepository) {
//
//         this.productRepository = productRepository;
//     }

     @Override
     public Product createProduct(Product newProduct) {

         return productRepository.save(newProduct);
     }

     @Override
     public ProductDTO updateProduct(Long id, ProductDTO updatedProduct) {
         // 1. Find existing product
         Product existingProduct = productRepository.findById(id)
                 .orElseThrow(() -> new ProductNotFoundException(id));

         // 2. Update fields (only those allowed)
         existingProduct.setName(updatedProduct.getName());
         existingProduct.setPrice(updatedProduct.getPrice());
         existingProduct.setDescription(updatedProduct.getDescription());
         existingProduct.setImageUrl(updatedProduct.getImageUrl());

         // 3. Save updated product
         Product saved = productRepository.save(existingProduct);

         // 4. Map back to DTO
         return cartMapper.toProductDTO(saved);
     }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return toDTO(product);
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);

//            RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

}


