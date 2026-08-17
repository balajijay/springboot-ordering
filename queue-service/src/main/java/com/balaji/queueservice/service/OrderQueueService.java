package com.balaji.queueservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balaji.queueservice.entity.Order;
import com.balaji.queueservice.entity.OrderLineItem;
import com.balaji.queueservice.repository.OrderLineItemRepository;
import com.balaji.queueservice.repository.OrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.math.*;

@Service
@Transactional(readOnly = true)
public class OrderQueueService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderQueueService.class);
	private final OrderRepository orderRepository;
	private final OrderLineItemRepository orderLineItemRepository;
	private final ObjectMapper objectMapper;
	
	public OrderQueueService (OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
			ObjectMapper objectMapper) {
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
		this.objectMapper = objectMapper;
	}
	
	public Order getOrderCompleteInfo(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new EntityNotFoundException("Order not found for " + orderId));
		orderLineItemRepository.findByOrderId(orderId);
		return order;
	}
	
	public void getAllOrderInfo() {
		final Map<String, String> orderRegistry = new ConcurrentHashMap<>();
		ExecutorService customThreadPool = Executors.newFixedThreadPool(3);
		List<Order> orderList = orderRepository.findAll();
		orderList.stream().forEach(order -> {
			CompletableFuture.supplyAsync(() -> order, customThreadPool)
            // Chains another async task to fetch payment details
            .thenApplyAsync(orderData -> processPayment(orderData), customThreadPool)
            // Safely handles unexpected runtime exceptions in the pipeline
            .exceptionally(ex -> "FAILED_ORDER: " + ex.getMessage())
            // Consumes the result and updates our thread-safe collection
            .thenAccept(finalResult -> {
                orderRegistry.put(order.getId().toString(), finalResult);
                System.out.println("[Thread " + Thread.currentThread().getName() + "] Registered: " + finalResult);
            });
		});
	}
	
	private static String processPayment(Order order) {
        simulateDelay(300); // Simulates banking gateway lag
        return  " Order processed ";
    }

	
	private static void simulateDelay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
	
	private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
	
	@SuppressWarnings({ "unchecked" })
	public List<OrderLineItem> saveOrderData(String orderPayload ) throws Exception {
		Map<String, Object> rawData = objectMapper.readValue(orderPayload, new TypeReference<>() {});
		Long orderId = ((Integer) rawData.get("id")).longValue();
		BigDecimal price = new BigDecimal((Double) rawData.get("price"));
		log.info("Order Id = {} ", orderId);
		log.info("Price= {} ", price);
		Integer quantity = (Integer) rawData.get("quantity");
		String status = (String) rawData.get("status");
		List<Map<String, Object>> lineItems = (List<Map<String, Object>>) rawData.get("lineItems");
		log.info("Order Line Items = {}", lineItems);
		return lineItems.stream().map(item -> new OrderLineItem(
                            		((Integer) item.get("id")).longValue(),
                                    (String) item.get("shortDesc"),
                                    (Integer) item.get("quantity"))).toList(); 
	}

}
