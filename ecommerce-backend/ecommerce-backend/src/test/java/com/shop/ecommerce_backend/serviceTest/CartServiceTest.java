package com.shop.ecommerce_backend.serviceTest;

import com.shop.ecommerce_backend.DTO.CartDTO;
import com.shop.ecommerce_backend.exception.*;
import com.shop.ecommerce_backend.DTO.CartMapper;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.CartItem;
import com.shop.ecommerce_backend.model.Product;
import com.shop.ecommerce_backend.repository.CartItemRepository;
import com.shop.ecommerce_backend.repository.CartRepository;
import com.shop.ecommerce_backend.repository.ProductRepository;
import com.shop.ecommerce_backend.service.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Unit Tests")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private Product testProduct;
    private String sessionToken;

    @BeforeEach
    void setUp() {
        sessionToken = "test_session_123";

        // Setup test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(99.99));


        // Setup test cart
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setSessionToken(sessionToken);
        testCart.setStatus(Cart.CartStatus.ACTIVE);
        testCart.setLastUpdated(LocalDateTime.now());
        testCart.setItems(new ArrayList<>());
    }

    // ========================================================================
    // GET CART TESTS
    // ========================================================================

    @Test
    @DisplayName("Should get cart by session token successfully")
    void shouldGetCartBySessionToken() {
        // Given
        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartMapper.toDTO(testCart)).thenReturn(new CartDTO());

        // When
        CartDTO result = cartService.fetchCart(sessionToken);

        // Then
        assertThat(result).isNotNull();
        verify(cartRepository).findBySessionToken(sessionToken);
        verify(cartMapper).toDTO(testCart);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when cart not found")
    void shouldThrowExceptionWhenCartNotFound() {
        // Given
        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.fetchCart(sessionToken))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cart")
                .hasMessageContaining(sessionToken);

        verify(cartRepository).findBySessionToken(sessionToken);
        verify(cartMapper, never()).toDTO(any());
    }

    // ========================================================================
    // ADD ITEM TESTS
    // ========================================================================

    @Test
    @DisplayName("Should add item to cart successfully")
    void shouldAddItemToCart() {
        // Given
        Long productId = 1L;
        Integer quantity = 2;

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(testProduct));
        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(testCart);
        when(cartMapper.toDTO(testCart)).thenReturn(new CartDTO());

        // When
        CartDTO result = cartService.addItemToCart(sessionToken, productId, quantity);

        // Then
        assertThat(result).isNotNull();
        verify(cartRepository).findBySessionToken(sessionToken);
        verify(productRepository).findById(productId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartRepository).save(testCart);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        Long productId = 999L;
        Integer quantity = 1;

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cartService.addItemToCart(sessionToken, productId, quantity))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(String.valueOf(productId));

        verify(productRepository).findById(productId);
        verify(cartItemRepository, never()).save(any());
    }



    // ========================================================================
    // UPDATE QUANTITY TESTS
    // ========================================================================

    @Test
    @DisplayName("Should update item quantity successfully")
    void shouldUpdateItemQuantity() {
        // Given
        Long productId = 1L;
        Integer newQuantity = 5;

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(testProduct);
        cartItem.setQuantity(2);
        testCart.getItems().add(cartItem);

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(testCart);
        when(cartMapper.toDTO(testCart)).thenReturn(new CartDTO());

        // When
        CartDTO result = cartService.updateItemQuantity(sessionToken, productId, newQuantity);

        // Then
        assertThat(result).isNotNull();
        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Should remove item when quantity is zero")
    void shouldRemoveItemWhenQuantityIsZero() {
        // Given
        Long productId = 1L;
        Integer newQuantity = 0;

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(testProduct);
        cartItem.setQuantity(2);
        testCart.getItems().add(cartItem);

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(testCart);
        when(cartMapper.toDTO(testCart)).thenReturn(new CartDTO());

        // When
        CartDTO result = cartService.updateItemQuantity(sessionToken, productId, newQuantity);

        // Then
        assertThat(result).isNotNull();
        assertThat(testCart.getItems()).isEmpty();
        verify(cartItemRepository).delete(cartItem);
    }

    // ========================================================================
    // REMOVE ITEM TESTS
    // ========================================================================

    @Test
    @DisplayName("Should remove item from cart successfully")
    void shouldRemoveItemFromCart() {
        // Given
        Long productId = 1L;

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(testProduct);
        testCart.getItems().add(cartItem);

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(testCart);
        when(cartMapper.toDTO(testCart)).thenReturn(new CartDTO());

        // When
        CartDTO result = cartService.removeItemFromCart(sessionToken, productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(testCart.getItems()).isEmpty();
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when item not in cart")
    void shouldThrowExceptionWhenItemNotInCart() {
        // Given
        Long productId = 999L;

        when(cartRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(testCart));

        // When & Then
        assertThatThrownBy(() -> cartService.removeItemFromCart(sessionToken, productId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("CartItem");

        verify(cartItemRepository, never()).delete(any());
    }
}
