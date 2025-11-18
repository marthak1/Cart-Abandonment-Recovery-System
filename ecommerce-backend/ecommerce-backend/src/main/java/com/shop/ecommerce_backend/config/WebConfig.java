package com.shop.ecommerce_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@org.springframework.lang.NonNull org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://frontend", "http://localhost", "http://127.0.0.1", "http://localhost:5173",
                                "http://localhost:4173",
                                "http://localhost:80",
                                "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3);
            }
        };
//         @Override
// public void addResourceHandlers(ResourceHandlerRegistry registry) {
//     registry.addResourceHandler("/static/**")
//             .addResourceLocations("classpath:/static/");



    }
}
