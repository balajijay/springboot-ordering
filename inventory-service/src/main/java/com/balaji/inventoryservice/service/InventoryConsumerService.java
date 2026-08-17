package com.balaji.inventoryservice.service;

import com.balaji.inventoryservice.entity.Inventory;
import com.balaji.inventoryservice.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class InventoryConsumerService {

	private static final Logger log = LoggerFactory.getLogger(InventoryConsumerService.class);
    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper; // Processes JSON strings easily
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public InventoryConsumerService(InventoryRepository inventoryRepository, ObjectMapper objectMapper, 
    		KafkaTemplate<String, String> kafkaTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    @Transactional
    public void consumeOrderEvent(String message) {
        try {
        	Map<String, Object> orderData = objectMapper.readValue(message, Map.class);
        	// 1. Safely extract Product ID using the Number utility bridge
        	Number prodIdNum = (Number) orderData.get("productId");
        	long productId = prodIdNum != null ? prodIdNum.longValue() : 0L;

        	// 2. Safely extract Quantity to protect against any unexpected type shifts
        	Number qtyNum = (Number) orderData.get("quantity");
        	Integer quantity = qtyNum != null ? qtyNum.intValue() : 0;

        	// 3. Safely extract Order ID using the exact same clean pattern
        	Number orderIdNum = (Number) orderData.get("id");
        	Long orderId = orderIdNum != null ? orderIdNum.longValue() : null;
           log.info("📬 Kafka Event Received! ProductId: " + productId + ", Qty: " + quantity);

            Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in inventory: " + productId));
            
            if (inventory.getAvailableStock() >= quantity) {
                inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
                inventoryRepository.save(inventory);
             // 2. Send Success Notification back to Kafka!
                String successMessage = String.format("{\"orderId\": %d, \"status\": \"SUCCESS\"}", orderId);
                kafkaTemplate.send("inventory-success-events", successMessage); // <-- Send confirmation!
                log.info("✅ Stock deducted! Remaining stock for " + productId + ": " + inventory.getAvailableStock());
            }  else {
                // Failure Path: Trigger Compensating Transaction!
                log.warn("❌ Out of stock for Order ID: " + orderId + ". Sending compensation event...");
                String rollbackMessage = String.format("{\"orderId\": %d, \"reason\": \"OUT_OF_STOCK\"}", orderId);
                kafkaTemplate.send("inventory-fail-events", rollbackMessage);
            }

        } catch (Exception e) {
           log.error("💥 Failed to process inventory update: " + e.getMessage());
        }
    }
}

