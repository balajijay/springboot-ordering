package com.balaji.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderStatusNotificationListener {

	private static final Logger log = LoggerFactory.getLogger(OrderStatusNotificationListener.class);
    
	private final ObjectMapper objectMapper;

    private final EmailService emailService;
    
    public OrderStatusNotificationListener(EmailService emailService, ObjectMapper objectMapper) {
    	this.emailService = emailService;
    	this.objectMapper = objectMapper;
    }

	@KafkaListener(topics = "order-status-events", groupId = "notification-group")
    public void consumeOrderStatusEvent(String payload) {
        try {
			Map<String, Object> orderData = objectMapper.readValue(payload, Map.class);
            Long orderId = Long.valueOf(orderData.get("id").toString()); // Extract the Order ID
            String status = (String) orderData.get("status");
            String email = (String) orderData.get("email");
            log.info("📩 Received order event notice for Order ID: {} with Status: {}", orderId, status);

            // Build out the contextual email notifications
            String recipient = email != null ? email : "customer@example.com";
            String subject = "Update regarding your Order #" + orderId;
            String body;

            switch (status) {
                case "CREATED" -> body = "Thank you! Your order #" + orderId + " has been successfully received.";
                case "COMPLETED" -> body = "Great news! Your order #" + orderId + " is confirmed and inventory has been reserved.";
                case "REJECTED" -> body = "We regret to inform you that your order #" + orderId + " was cancelled due to stock limitations.";
                default -> {
                    log.warn("No custom template mapped for status: {}", status);
                    return;
                }
            }

            emailService.sendNotificationEmail(recipient, subject, body);

        } catch (Exception e) {
            log.error("Failed to parse or process Kafka notification event payload: " + payload, e);
        }
    }
}