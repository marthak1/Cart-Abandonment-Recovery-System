package com.shop.ecommerce_backend.serviceTest;

import com.shop.ecommerce_backend.exception.*;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.repository.CartRepository;
import com.shop.ecommerce_backend.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
// import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("CartRecovery Unit Tests")
class CartRecoveryTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private String sessionToken;

    @BeforeEach
    void setUp() {
        sessionToken = "test_session_123";

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setSessionToken(sessionToken);
        testCart.setStatus(Cart.CartStatus.ACTIVE);
        testCart.setLastUpdated(LocalDateTime.now());
        testCart.setRecoveryFlag(false);
    }

@Test
void detectAbandonedCarts_shouldMarkCartAsAbandoned() {
    Cart cart = new Cart();
    cart.setSessionToken("test-session");
    CartItem item = new CartItem();
    cart.setItems(List.of(item)); // mock item
    cart.setLastUpdated(LocalDateTime.now().minusMinutes(2));

    when(cartRepository.findInactiveCarts(eq(Cart.CartStatus.ACTIVE), any()))
            .thenReturn(List.of(cart));

    cartService.detectAbandonedCarts();

    verify(cartRepository).save(cart);
    assertThat(cart.getStatus() == Cart.CartStatus.ABANDONED);
    assertThat(cart.getRecoveryFlag());
    assertThat(cart.getAbandonedAt() != null);
}

    @Test
    void detectAbandonedCarts_shouldThrowCartRecoveryException_onSaveFailure() {
        Cart cart = new Cart();
        cart.setSessionToken("fail-session");
        CartItem item = new CartItem();
        cart.setItems(List.of(item));
        cart.setLastUpdated(LocalDateTime.now().minusMinutes(2));

        when(cartRepository.findInactiveCarts(eq(Cart.CartStatus.ACTIVE), any()))
                .thenReturn(List.of(cart));
        doThrow(new RuntimeException("DB error")).when(cartRepository).save(cart);

        try {
            cartService.detectAbandonedCarts();
            assert false : "Expected CartRecoveryException";
        } catch (CartRecoveryException e) {
            assert e.getMessage().contains("fail-session");
        }
    }

    @Test
    @DisplayName("Should dismiss recovery successfully")
    void shouldDismissRecovery() {
        // Given
        testCart.setStatus(Cart.CartStatus.ABANDONED);
        testCart.setRecoveryFlag(true);

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // When
        cartService.resetRecoveryFlag(sessionToken);

        // Then
        assertThat(testCart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);
        assertThat(testCart.getRecoveryFlag()).isFalse();
        verify(cartRepository).save(testCart);
    }

    @Test
    @DisplayName("Should mark cart as recovered successfully")
    void shouldMarkCartAsRecovered() {
        // Given
        testCart.setStatus(Cart.CartStatus.ABANDONED);
        testCart.getItems().add(new CartItem());

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // When
        cartService.markCartAsRecovered(sessionToken);

        // Then
        assertThat(testCart.getStatus()).isEqualTo(Cart.CartStatus.RECOVERED);
        assertThat(testCart.getRecoveryFlag()).isFalse();
        verify(cartRepository).save(testCart);
    }
}

