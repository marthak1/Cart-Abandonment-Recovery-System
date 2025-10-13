 package com.shop.ecommerce_backend.service;

 import com.shop.ecommerce_backend.model.Cart;
 import com.shop.ecommerce_backend.model.CartItem;
 import com.shop.ecommerce_backend.model.Product;
 import com.shop.ecommerce_backend.repository.CartRepository;
 import com.shop.ecommerce_backend.repository.ProductRepository;
 import org.springframework.stereotype.Service;
 import java.time.LocalDateTime;
 import java.util.List;
 import java.util.Optional;
// TODO: CREATE A GLOBAL EXCEPTION
 @Service
 public class CartItemServiceImpl implements ICartItemService {

     private final CartRepository cartRepository;
     private final ProductRepository productRepository;

     public CartItemServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
         this.cartRepository = cartRepository;
         this.productRepository = productRepository;
     }

     @Override
     public Cart addItemToCart(String sessionToken, CartItem newItem) {
         Cart cart = cartRepository.findBySessionToken(sessionToken)
                 .orElseThrow(() -> new RuntimeException("Cart not found"));
                         //new CartNotFoundException(sessionToken));

         Product product = productRepository.findById(newItem.getProduct().getId())
                 .orElseThrow(() -> new RuntimeException("Product not found"));
                         //new ProductNotFoundException(newItem.getProduct().getId()));

         Optional<CartItem> existingItem = cart.getItems().stream()
                 .filter(item -> item.getProduct().getId().equals(product.getId()))
                 .findFirst();

         if (existingItem.isPresent()) {
             existingItem.get().setQuantity(existingItem.get().getQuantity() + newItem.getQuantity());
         } else {
             newItem.setProduct(product);
             cart.getItems().add(newItem);
         }

         cart.setLastUpdated(LocalDateTime.now());
         return cartRepository.save(cart);
     }

     @Override
     public Cart removeItemFromCart(String sessionToken, Long productId) {
         Cart cart = cartRepository.findBySessionToken(sessionToken)
                 .orElseThrow(() -> new RuntimeException("Cart not found"));
                         //new CartNotFoundException(sessionToken));

         cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
         cart.setLastUpdated(LocalDateTime.now());
         return cartRepository.save(cart);
     }

     @Override
     public Cart updateItemQuantity(String sessionToken, Long productId, int quantity) {
         if (quantity <= 0) throw new RuntimeException("Invalid quantity");
                 //new InvalidQuantityException();

         Cart cart = cartRepository.findBySessionToken(sessionToken)
                 .orElseThrow(() -> new RuntimeException("Cart not found"));
                         //new CartNotFoundException(sessionToken));

         cart.getItems().stream()
                 .filter(item -> item.getProduct().getId().equals(productId))
                 .findFirst()
                 .ifPresent(item -> item.setQuantity(quantity));

         cart.setLastUpdated(LocalDateTime.now());
         return cartRepository.save(cart);
     }

     @Override
     public List<CartItem> getItems(String sessionToken) {
         Cart cart = cartRepository.findBySessionToken(sessionToken)
                 .orElseThrow(() -> new RuntimeException("Cart not found"));
                         //new CartNotFoundException(sessionToken));
         return cart.getItems();
     }

     @Override
     public Cart clearItems(String sessionToken) {
         Cart cart = cartRepository.findBySessionToken(sessionToken)
                 .orElseThrow(() -> new RuntimeException("Cart not found"));
                         //new CartNotFoundException(sessionToken));
         cart.getItems().clear();
         cart.setLastUpdated(LocalDateTime.now());
         return cartRepository.save(cart);
     }
 }


// CartItemService
// **Purpose:**
// Handles operations related to individual items within a cart.
//**Typical Logic:**
//- Add an item to a cart
//- Update the quantity of an item in a cart
//- Remove an item from a cart
//- Retrieve all items in a cart
//- Find a specific item in a cart
