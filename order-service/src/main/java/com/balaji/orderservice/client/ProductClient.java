package com.balaji.orderservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.balaji.orderservice.dto.ProductExistsResponseDto;

import java.util.List;

// 1. Target the path natively using Spring Boot 4 HttpExchange
@HttpExchange("/api/v1/products")
public interface ProductClient {

    @GetExchange
    List<Object> getAllProducts(); // Replace Object with your actual DTO

    @GetExchange("/{id}/exists")
    ProductExistsResponseDto checkProductExists(@PathVariable("id") long id);
}
