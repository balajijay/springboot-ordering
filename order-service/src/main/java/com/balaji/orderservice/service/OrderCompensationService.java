package com.balaji.orderservice.service;

import com.balaji.orderservice.entity.OrderStatus;
import com.balaji.orderservice.entity.Outbox;
import com.balaji.orderservice.repository.OrderRepository;
import com.balaji.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderCompensationService {

	private static final Logger log = LoggerFactory.getLogger(OrderCompensationService.class);
    private OrderRepository orderRepository;    
    private ObjectMapper objectMapper;
    private OutboxRepository outboxRepository;
    
    public OrderCompensationService(OrderRepository orderRepository, ObjectMapper objectMapper
    		,OutboxRepository outboxRepository) {
    		this.orderRepository = orderRepository;
    		this.objectMapper = objectMapper;
    		this.outboxRepository = outboxRepository;
    }

    @KafkaListener(topics = "inventory-fail-events", groupId = "order-compensation-group")
    @Transactional
    public void processOrderRollback(String message) {
        try {
            Map<String, Object> failData = objectMapper.readValue(message, Map.class);
            Long orderId = Long.valueOf(failData.get("orderId").toString());
            String reason = (String) failData.get("reason");

            log.info("⚠️ Received Compensation Event! Reverting Order ID: " + orderId + " due to: " + reason);

            // Fetch the stuck order and flag it as rejected
            orderRepository.findById(orderId).ifPresent(order -> {
                order.setStatus(OrderStatus.REJECTED); // Ensure you have a 'status' field in your Order Entity!
                orderRepository.save(order);
                Outbox outbox = new Outbox();
                outbox.setAggregateType("Order");
                outbox.setAggregateId(order.getId().toString());
                outbox.setEventType("OrderRejected");
                try {
					outbox.setPayload(objectMapper.writeValueAsString(order));
				} catch (JsonProcessingException e) {
					log.error("Failed to serialize order object for Outbox ID: " + orderId, e);
			        throw new RuntimeException("Serialization failure", e); 
				}
                outboxRepository.save(outbox);
                log.info("🛑 Order ID " + orderId + " status explicitly flipped to REJECTED.");
            });

        } catch (Exception e) {
            log.error("💥 Failed to process compensation: " + e.getMessage());
        }
    }
    
    @KafkaListener(topics = "inventory-success-events", groupId = "order-success-group")
    @Transactional
    public void processOrderSuccess(String message) {
        try {
            Map<String, Object> successData = objectMapper.readValue(message, Map.class);
            Long orderId = Long.valueOf(successData.get("orderId").toString());

            log.info("🎉 Received Success Confirmation from Inventory for Order ID: " + orderId);

            // Fetch the order and move it safely to COMPLETED
            orderRepository.findById(orderId).ifPresent(order -> {
                order.setStatus(OrderStatus.COMPLETED); // <-- Explicitly set to COMPLETED!
                orderRepository.save(order);
                Outbox outbox = new Outbox();
                outbox.setAggregateType("Order");
                outbox.setAggregateId(order.getId().toString());
                outbox.setEventType("OrderCompleted");
                try {
					outbox.setPayload(objectMapper.writeValueAsString(order));
				} catch (JsonProcessingException e) {
					log.error("Failed to serialize order object for Outbox ID: " + orderId, e);
			        throw new RuntimeException("Serialization failure", e); 
				}
                outboxRepository.save(outbox);
                log.info("💚 Order ID " + orderId + " is now permanently marked as COMPLETED.");
            });

        } catch (Exception e) {
            log.error("💥 Failed to mark order as completed: " + e.getMessage());
        }
    }

    
}
