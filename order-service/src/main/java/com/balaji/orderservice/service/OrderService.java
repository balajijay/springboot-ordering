package com.balaji.orderservice.service;

import com.balaji.orderservice.client.ProductClient;
import com.balaji.orderservice.dto.ProductExistsResponseDto;
import com.balaji.orderservice.entity.Order;
import com.balaji.orderservice.entity.OrderStatus;
import com.balaji.orderservice.entity.Outbox;
import com.balaji.orderservice.repository.OrderRepository;
import com.balaji.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.math.*;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final ProductClient productClient;
    private final KafkaTemplate kafkaTemplate;
    
    @Autowired
    @Lazy
    private OrderService self;
    
    public OrderService(OrderRepository orderRepository, 
            OutboxRepository outboxRepository, ObjectMapper objectMapper,
            ProductClient productClient, KafkaTemplate kafkaTemplate ) {
    			this.orderRepository = orderRepository;
    			this.outboxRepository = outboxRepository;
    			this.objectMapper = objectMapper;
    			this.productClient = productClient;
    			this.kafkaTemplate = kafkaTemplate;
    		}
    
    public CompletableFuture<Map<OrderStatus, BigDecimal>> processOrdersAsync(List<Order> orders, ExecutorService executor) {
        // SupplyAsync runs the stream processing code inside the custom thread pool
        return CompletableFuture.supplyAsync(() -> {
            return orders.stream()
                .collect(Collectors.groupingBy(
                    Order::getStatus,
                    Collectors.mapping(
                            Order::getPrice, 
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        }, executor); // Passes the custom executor here
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders(){
    	return orderRepository.findAll();  }
    
    public Order createOrder(Order order) throws JsonProcessingException  {
    	//validate order before accepting it
    	log.info("---> Entering createOrder service method for product: {}", order.getProductId());
    	try {
    	ProductExistsResponseDto productExistsResponseDto = productClient.checkProductExists(order.getProductId());
    	log.info("---> ProductClient responded: {}", productExistsResponseDto);
        if (productExistsResponseDto == null || !productExistsResponseDto.exists()) {
        	log.info("Order service product client returned null or product does not exist ");
            throw new IllegalArgumentException("Validation Failed: Product ID " + order.getProductId() + " does not exist in the database.");
        }
        log.info("---> Attempting to save Order entity...");
    	return self.saveOrderAndOutbox(order);}
    	catch (Throwable te) {
    		log.error("!!! CRITICAL EXCEPTION CAUGHT INSIDE createOrder VALIDATION LOOP !!!", te);
            throw te;
    	}
    }
    
    @Transactional
    public Order saveOrderAndOutbox(Order order) throws JsonProcessingException {
    	order.setStatus(OrderStatus.CREATED);
        Order savedOrder = orderRepository.save(order);
        log.info("---> Order saved, attempting to save Outbox entity...");
        Outbox outbox = new Outbox();
        outbox.setAggregateType("Order");
        outbox.setAggregateId(savedOrder.getId().toString());
        outbox.setEventType("OrderCreated");
        outbox.setPayload(objectMapper.writeValueAsString(savedOrder));
        
        outboxRepository.save(outbox);
        log.info("---> Outbox saved in memory successfully! Exiting method...");
        return savedOrder;
    	
    }
    
    public CompletableFuture<Order> fetchOrder(Long id) {
    	return CompletableFuture.supplyAsync(() -> orderRepository.findById(id).orElseThrow());
    }
    
    public CompletableFuture<Void> publishOrderEvent (Order order) {
    	return CompletableFuture.runAsync(() -> {
    		kafkaTemplate.send("Order-Event", order.getId().toString(), order.getId().toString() );
    	});
    }
    
    public CompletableFuture<Void> processOrder(Order order) {	
    	return CompletableFuture.runAsync(() -> publishOrderEvent(order).exceptionally(ex -> {
    		return null;
    	}));
     } 
    
} 
