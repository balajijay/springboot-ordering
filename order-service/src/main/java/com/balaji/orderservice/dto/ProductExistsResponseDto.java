package com.balaji.orderservice.dto;

// A Java Record is perfect for clean, production-grade read-only data
public record ProductExistsResponseDto(boolean exists, long productId) {}
