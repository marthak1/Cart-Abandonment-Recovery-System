package com.shop.ecommerce_backend.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.shop.ecommerce_backend.DTO.CartDTO;
import com.shop.ecommerce_backend.DTO.CartMapper;
import com.shop.ecommerce_backend.exception.CartRecoveryException;
import com.shop.ecommerce_backend.exception.InvalidCartException;
import com.shop.ecommerce_backend.exception.ProductNotFoundException;
import com.shop.ecommerce_backend.exception.ResourceNotFoundException;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.model.Product;
import com.shop.ecommerce_backend.repository.CartItemRepository;
import com.shop.ecommerce_backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;



//TODO: CREATE A GLOBAL EXCEPTION

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CartServiceImpl implements ICartService {
    // Inject repositories
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDTO addItemToCart(String sessionToken, Long productId, Integer quantity) {
        // 1. Validate session token
        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new InvalidCartException("Session token is required");
        }

        // 2. Validate quantity
        if (quantity == null || quantity <= 0) {
            throw new InvalidCartException("Quantity must be greater than 0");
        }

        if (quantity > 100) {
            throw new InvalidCartException("Cannot add more than 100 items at once");
        }
        System.out.println("Finding cart for session: " + sessionToken);
        // Find or create cart
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseGet(() -> {
                    System.out.println("Creating new cart for session: " + sessionToken);
                    Cart newCart = new Cart();
                    newCart.setSessionToken(sessionToken);
                    newCart.setStatus(Cart.CartStatus.valueOf("ACTIVE"));
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setLastUpdated(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->  new ProductNotFoundException(productId));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            System.out.println("Updated existing item, new quantity: " + item.getQuantity());
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());

            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
            System.out.println("Added new item to cart");
        }

        // Update cart timestamp
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);

        // Reload cart to ensure all relationships are loaded
        cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart",
                        "sessionToken",
                        sessionToken
                ));

        System.out.println("Final cart has " + cart.getItems().size() + " items");

        // Convert to DTO with all items
        CartDTO dto = cartMapper.toDTO(cart);
        dto.setTotal(calculateTotal(cart));

        return dto;
    }
    @Override
    public CartDTO fetchCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart",
                        "sessionToken",
                        sessionToken
                ));

        cart.setLastUpdated(LocalDateTime.now()); // lifecycle trace
        cartRepository.save(cart); // persist timestamp

        CartDTO dto = cartMapper.toDTO(cart);
        dto.setTotal(calculateTotal(cart)); // inject recalculated total
//        String status = cart.getItems().isEmpty() ? "EMPTY" : "ACTIVE";
//        dto.setStatus(status);
        return dto;
    }
    @Override
    @Transactional
    public CartDTO removeItemFromCart(String sessionToken, Long productId) {
        // Find cart with items loaded
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart",
                        "sessionToken",
                        sessionToken
                ));

        System.out.println("Looking for product " + productId + " in " + cart.getItems().size() + " items");

        // Find and remove the item
        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CartItem",
                        "productId",
                        productId
                ));

        System.out.println("Removing item: " + itemToRemove.getId());

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
    // Validate quantity
    if (quantity == null || quantity < 0) {
        throw new InvalidCartException("Quantity cannot be negative");
    }
        // JOIN FETCH query to load product data
    Cart cart = cartRepository.findBySessionToken(sessionToken)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Cart",
                    "sessionToken",
                    sessionToken
            ));

    Optional<CartItem> optionalItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst();

    if (optionalItem.isEmpty()) {
        throw  new ResourceNotFoundException(
                "CartItem",
                "productId",
                productId
        );
    }
    CartItem item = optionalItem.get();

// If quantity is 0, remove item
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart",
                        "sessionToken",
                        sessionToken
                ));
        cartRepository.delete(cart);
    }
    @Transactional
    public CartDTO clearCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Cart",
                    "sessionToken",
                    sessionToken
            ));

        cartItemRepository.deleteAllByCart(cart); // custom method
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.toDTO(cart);
    }

    // Check if cart is inactive and mark for recovery
    @Transactional
    public boolean isCartInactive(String sessionToken, Duration threshold) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart",
                        "sessionToken",
                        sessionToken
                ));

        LocalDateTime cutoff = LocalDateTime.now().minus(threshold);

        // If cart is active but hasn't been updated since cutoff
        if (cart.getStatus() == Cart.CartStatus.ACTIVE &&
                cart.getLastUpdated().isBefore(cutoff) &&
                !cart.getItems().isEmpty()) { // Only mark if cart has items

            cart.setStatus(Cart.CartStatus.ABANDONED);
            cart.setRecoveryFlag(true); // Signal frontend to show modal
            cart.setAbandonedAt(LocalDateTime.now());
            cartRepository.save(cart);

            System.out.println("Cart marked as abandoned: " + cart.getSessionToken());
            return true;
        }

        return false;
    }

    // Reset recovery flag when user interacts
    @Transactional
    public void resetRecoveryFlag(String sessionToken) {
        cartRepository.findBySessionToken(sessionToken).ifPresent(cart -> {
            cart.setRecoveryFlag(false);
            cart.setStatus(Cart.CartStatus.ACTIVE);
            cart.setLastUpdated(LocalDateTime.now());
            cartRepository.save(cart);
            System.out.println("Recovery flag reset for: " + sessionToken);
        });
    }

    //  Mark cart as recovered when user responds to modal
    @Transactional
    public void markCartAsRecovered(String sessionToken) {
        cartRepository.findBySessionToken(sessionToken).ifPresent(cart -> {
            cart.setStatus(Cart.CartStatus.RECOVERED);
            cart.setRecoveryFlag(false);
            cart.setLastUpdated(LocalDateTime.now());
            cartRepository.save(cart);
            System.out.println("Cart recovered: " + sessionToken);
        });
    }

    // Update last activity timestamp
    @Transactional
    public void updateCartActivity(String sessionToken) {
        cartRepository.findBySessionToken(sessionToken).ifPresent(cart -> {
            cart.setLastUpdated(LocalDateTime.now());

            // Reset to active if it was abandoned
            if (cart.getStatus() == Cart.CartStatus.ABANDONED) {
                cart.setStatus(Cart.CartStatus.ACTIVE);
                cart.setRecoveryFlag(false);
            }

            cartRepository.save(cart);
        });
    }

    // Scheduled task - Check every 10 seconds (for testing)
    // Change to @Scheduled(fixedRate = 60000) for production (1 minute)
    @Scheduled(fixedRate = 10000) // 3600000 every hour
    @Transactional
    public void detectAbandonedCarts() {
        System.out.println("[" + LocalDateTime.now() + "] Checking for abandoned carts...");
        // Find carts that haven't been updated in 1 minute
        LocalDateTime cutoff = LocalDateTime.now().minus(Duration.ofMinutes(1));
        List<Cart> inactiveCarts = cartRepository.findInactiveCarts(Cart.CartStatus.ACTIVE, cutoff);

        System.out.println("Found " + inactiveCarts.size() + " inactive carts");

        for (Cart cart : inactiveCarts) {
            try {
                if (!cart.getItems().isEmpty()) {  // Only mark carts with items
                    System.out.println("   Marking cart as abandoned:");
                    System.out.println("   Session: " + cart.getSessionToken());
                    System.out.println("   Items: " + cart.getItems().size());
                    System.out.println("   Last Updated: " + cart.getLastUpdated());
                    System.out.println("   Setting recoveryFlag = true");

                    cart.setStatus(Cart.CartStatus.ABANDONED);
                    cart.setRecoveryFlag(true);  // frontend modal trigger
                    cart.setAbandonedAt(LocalDateTime.now());
                    cartRepository.save(cart);
                    System.out.println("Cart saved with recoveryFlag = true");
                }
            } catch(Exception e){
                throw new CartRecoveryException("Cart abandonment detection failed: " + cart.getSessionToken(), e);
            }
            }

    }

}

