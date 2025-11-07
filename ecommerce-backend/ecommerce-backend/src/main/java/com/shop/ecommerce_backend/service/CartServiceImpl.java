package com.shop.ecommerce_backend.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import com.shop.ecommerce_backend.DTO.CartDTO;
import com.shop.ecommerce_backend.DTO.CartMapper;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.model.Product;
import com.shop.ecommerce_backend.repository.CartItemRepository;
import com.shop.ecommerce_backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;


//TODO: CREATE A GLOBAL EXCEPTION

@Service
@Slf4j
@AllArgsConstructor
public class CartServiceImpl implements ICartService {
    // Inject repositories
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDTO addItemToCart(String sessionToken, Long productId, Integer quantity) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setSessionToken(sessionToken);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setLastUpdated(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProductId(cart, productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }

        cart.setLastUpdated(LocalDateTime.now());
        Cart updatedCart = cartRepository.findBySessionToken(sessionToken).get();
        CartDTO dto = cartMapper.toDTO(updatedCart); //map updated cart to dto
        dto.setTotal(calculateTotal(updatedCart)); // inject total
        return dto;
    }
    @Override
    public CartDTO fetchCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        cart.setLastUpdated(LocalDateTime.now()); // lifecycle trace
        cartRepository.save(cart); // persist timestamp

        CartDTO dto = cartMapper.toDTO(cart);
        dto.setTotal(calculateTotal(cart)); // inject recalculated total
        String status = cart.getItems().isEmpty() ? "EMPTY" : "ACTIVE";
        dto.setStatus(status);
        return dto;
    }
    @Override
    @Transactional
    public CartDTO removeItemFromCart(String sessionToken, Long productId) {
        // Find cart with items loaded
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cart not found"
                ));

        System.out.println("🔍 Looking for product " + productId + " in " + cart.getItems().size() + " items");

        // Find and remove the item
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product " + productId + " not found in cart"
                ));

        System.out.println("🗑️ Removing item: " + itemToRemove.getId());

        // Remove from collection first
        cart.getItems().remove(itemToRemove);

        // Then delete from database
        cartItemRepository.delete(itemToRemove);

        // Update cart timestamp
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);

        System.out.println("Cart now has " + cart.getItems().size() + " items");

        // Return updated cart DTO
        CartDTO dto = cartMapper.toDTO(cart);
        dto.setTotal(calculateTotal(cart));

        return dto;
    }


@Override
@Transactional
public CartDTO updateItemQuantity(String sessionToken, Long productId, Integer quantity) {
    // JOIN FETCH query to load product data
    Cart cart = cartRepository.findBySessionToken(sessionToken)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cart not found for session: " + sessionToken
            ));

    Optional<CartItem> optionalItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst();

    if (optionalItem.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product " + productId + " not found in cart"
        );
    }

    CartItem item = optionalItem.get();

    if (quantity == 0) {
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
    } else {
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    cart.setLastUpdated(LocalDateTime.now());
    cartRepository.save(cart);

    // Mapper to include product details
    CartDTO dto = cartMapper.toDTO(cart);
    dto.setTotal(calculateTotal(cart));

    return dto;
}


    // Calculate total helper method

    private BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public void deleteCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }


    //Trigger recovery modal if cart is inactive
    @Override
    public boolean isCartInactive(String sessionToken, Duration threshold) {
        Cart cart = cartRepository.findBySessionToken(sessionToken).orElseThrow();
        LocalDateTime cutoff = LocalDateTime.now().minus(threshold);
        return !cart.isCheckedOut() && cart.getLastUpdated().isBefore(cutoff);

    }
    @Transactional
    public void clearAllCartData() {
        cartItemRepository.deleteAll(); // delete items first to avoid FK constraint
        cartRepository.deleteAll();     // then delete carts
    }

}

