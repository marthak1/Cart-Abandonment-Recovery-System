package com.shop.ecommerce_backend;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
// This test starts the Spring Boot app on a random port, sends a GET request to /api/hello, and checks that the response is "Hello, World!".
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloTest {

    @Autowired 
    private TestRestTemplate restTemplate; // a spring utility for making http request in integration test

    @Test 
    void helloEndpointReturnsHelloWorld() {
        String body = this.restTemplate.getForObject("/api/hello", String.class);
        assertThat(body).isEqualTo("Hello, World!");
    }
}
