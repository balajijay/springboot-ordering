package com.balaji.inventoryservice.consumer;

import com.balaji.inventoryservice.model.ProductCreatedEvent;
import com.balaji.inventoryservice.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// ⚠️ Make sure to import your existing Inventory Entity and Repository here!
 import com.balaji.inventoryservice.entity.Inventory;

@Component
@Slf4j
public class ProductCatalogConsumer {
	
	private static final Logger log = LoggerFactory.getLogger(ProductCatalogConsumer.class);

    private final ObjectMapper objectMapper;
    
    private final InventoryRepository inventoryRepository; 
    
    public ProductCatalogConsumer (InventoryRepository inventoryRepository, ObjectMapper objectMapper) {
    	this.inventoryRepository = inventoryRepository;
    	this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "product-catalog-events", groupId = "inventory-catalog-group")
    @Transactional
    public void consumeProductCreatedEvent(String rawPayload) {
        try {
            // 1. Convert the incoming JSON payload string back into a Java Object context
            ProductCreatedEvent event = objectMapper.readValue(rawPayload, ProductCreatedEvent.class);
            log.info("📥 Inventory Service caught new product release for SKU: {}", event.getSku());

            // 2. Check your H2 Database to prevent duplicate entries if Kafka retries
            Inventory existingStock = inventoryRepository.findBySku(event.getSku());
            if (existingStock == null) {
                // 3. Initialize the product SKU in your warehouse inventory shelves with 0 items
                Inventory initialInventory = new Inventory();
                initialInventory.setProductId(event.getProductId());
                initialInventory.setSku(event.getSku());
                initialInventory.setAvailableStock(0); // 👈 Set to 0! Ready for a future stock upload
                
                inventoryRepository.save(initialInventory);
                log.info("📦 Successfully mapped SKU [{}] to H2 inventory inventory shelves with 0 starting items.", event.getSku());
            } else {
                log.warn("⚠️ SKU [{}] already exists in warehouse storage directory. Skipping initialization.", event.getSku());
            }

        } catch (Exception e) {
            log.error("❌ Critical processing failure inside Inventory Service product-catalog consumer loop", e);
        }
    }
}
