package com.shop.ecommerce_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.ecommerce_backend.model.Cart;
import com.shop.ecommerce_backend.model.Product;
import com.shop.ecommerce_backend.repository.CartRepository;
import com.shop.ecommerce_backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("CartController Integration Tests")
class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    private String sessionToken;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll(); // clean slate
        sessionToken = "test_session_" + System.currentTimeMillis();

        // Create test product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        testProduct = productRepository.save(testProduct);

        // Create test cart
        testCart = new Cart();
        testCart.setSessionToken(sessionToken);
        testCart.setStatus(Cart.CartStatus.valueOf("ACTIVE"));
        testCart.setSessionToken(sessionToken);
        testCart.setCreatedAt(LocalDateTime.now());
        testCart.setLastUpdated(LocalDateTime.now());
        testCart = cartRepository.save(testCart);
    }

    @Test
    @DisplayName("Should get cart successfully")
    void shouldGetCart() throws Exception {
        mockMvc.perform(get("/api/cart/" + sessionToken)
                        .header("X-Session-Token", sessionToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionToken").value(sessionToken))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should add item to cart successfully")
    void shouldAddItemToCart() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", testProduct.getId());
        request.put("quantity", 2);

        mockMvc.perform(post("/api/cart/addItem")
                        .header("X-Session-Token", sessionToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", 999L);
        request.put("quantity", 1);

        mockMvc.perform(post("/api/cart/addItem")
                        .header("X-Session-Token", sessionToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product Not Found"));
    }

//    @Test
//    @DisplayName("Should return 400 for insufficient stock")
//    void shouldReturn400ForInsufficientStock() throws Exception {
//        Map<String, Object> request = new HashMap<>();
//        request.put("productId", testProduct.getId());
//        request.put("quantity", 20); // More than available
//
//        mockMvc.perform(post("/cart/addItem")
//                        .header("X-Session-Token", sessionToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("Insufficient Stock"));
//    }

    @Test
    @DisplayName("Should update item quantity successfully")
    void shouldUpdateItemQuantity() throws Exception {
        // First add item
        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put("productId", testProduct.getId());
        addRequest.put("quantity", 2);

        mockMvc.perform(post("/api/cart/addItem")
                .header("X-Session-Token", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)));

        // Then update quantity
        Map<String, Integer> updateRequest = new HashMap<>();
        updateRequest.put("quantity", 5);

        mockMvc.perform(put("/api/cart/" + sessionToken + "/item/" + testProduct.getId())
                        .header("X-Session-Token", sessionToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    @DisplayName("Should remove item from cart successfully")
    void shouldRemoveItemFromCart() throws Exception {
        // First add item
        Map<String, Object> addRequest = new HashMap<>();
        addRequest.put("productId", testProduct.getId());
        addRequest.put("quantity", 2);

        mockMvc.perform(post("/api/cart/addItem")
                .header("X-Session-Token", sessionToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequest)));

        // Then remove item
        mockMvc.perform(delete("/api/cart/" + sessionToken + "/item/" + testProduct.getId())
                        .header("X-Session-Token", sessionToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}