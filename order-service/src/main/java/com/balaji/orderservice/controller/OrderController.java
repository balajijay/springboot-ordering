package com.balaji.orderservice.controller;

import com.balaji.orderservice.entity.Order;
import com.balaji.orderservice.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
    	this.orderService = orderService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'ORDER_USER')")
    public List<Order> getOrders(){
    	return orderService.getOrders();
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'ORDER_USER')")
    public ResponseEntity<?> placeOrder(
            @RequestBody Order order, 
            @RequestHeader(value = "X-Gateway-Validation", required = false) String gatewayHeader,
            @RequestHeader(value = "Authorization", required = false) String authHeader) { // Automatically captures the token forwarded by the gateway
        try {
        	if (gatewayHeader == null || !gatewayHeader.contains("SecuredByGateway")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Direct access forbidden.");
            }
            String userEmail = "system";
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String[] chunks = token.split("\\.");
                if (chunks.length > 1) {
                    String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));       
                    log.info("DEBUG Azure Payload: " + payload);
                    if (payload.contains("\"sub\"")) {
                        userEmail = payload.substring(payload.indexOf("\"sub\"") + 7, payload.indexOf("\"", payload.indexOf("\"sub\"") + 7));
                    }
                }
            }            
            log.info("👤 Order processed for User Identity: [" + userEmail + "]");

            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
