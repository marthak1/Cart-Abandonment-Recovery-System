package com.shop.ecommerce_backend.service;

import org.springframework.stereotype.Service;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.repository.CartRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


//TODO: CREATE A GLOBAL EXCEPTION

@Service
public class CartServiceImpl implements ICartService {
    // Inject repositories
    private final CartRepository cartRepository;
    // repository injection for DB operations


    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
        // constructor injection for better testability
    }
    @Override
    public Cart createCart(String sessionToken) {
        Cart cart = new Cart(sessionToken);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartBySessionToken(String sessionToken) {
        return cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
//                        new CartNotFoundException(sessionToken)); -> use this for global exception
    }



    @Override
    public Cart clearCart(Cart cart) {
        cart.setItems(new ArrayList<>()); //initializing items in the constructor to avoid null pointer
       return cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateCartTotal(String  sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public Cart updateCart(Cart cart) {
        cart.setLastUpdated(LocalDateTime.now());
        return cartRepository.save(cart);
    }


    @Override
    public Cart checkoutCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setCheckedOut(true);
        cart.setLastUpdated(LocalDateTime.now());
        return cartRepository.save(cart);
    }


    @Override
    public void deleteCart(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }

    //Associate cart with user session
    @Override
    public Cart assignCartToUserSession(String sessionToken) {
        Cart cart = cartRepository.findBySessionToken(sessionToken)
                .orElseGet(() -> cartRepository.save(new Cart(sessionToken)));
        cart.setLastUpdated(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    //Trigger recovery modal if cart is inactive
    @Override
    public boolean isCartInactive(String sessionToken, Duration threshold) {
        Cart cart = cartRepository.findBySessionToken(sessionToken).orElseThrow();
        LocalDateTime cutoff = LocalDateTime.now().minus(threshold);
        return !cart.isCheckedOut() && cart.getLastUpdated().isBefore(cutoff);

    }
}
//CartService
// **Purpose:**
//   Handles operations related to the entire cart, such as creating, retrieving, updating, and checking out a cart.
//**Typical Logic:**
//- Create a new cart (for a session/user)
//- Retrieve a cart by session token or user ID
//- Update cart properties (e.g., mark as checked out)
//- Clear a cart (remove all items)
//- Calculate cart totals (subtotal, tax, etc.)
//- Delete a cart
