package com.balaji.inventoryservice.controller;

import com.balaji.inventoryservice.entity.Inventory;
import com.balaji.inventoryservice.service.InventoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	private static final Logger log = LoggerFactory.getLogger(InventoryController.class);
	
	private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // 1. Get all inventory items to verify stock levels
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'INVENTORY_USER')")
    public List<Inventory> getAllInventory() {
        return inventoryService.findAll();
    }

    // 2. Add or initialize stock for a product (Great for testing!)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'INVENTORY_USER')")
    public ResponseEntity<?> addStock(@RequestBody Inventory inventory, 
    		@RequestHeader(value = "X-Gateway-Validation", required = false) String gatewayHeader,
    		@RequestHeader(value = "Authorization", required = false) String authHeader) {
    	log.info("DEBUG: Received Gateway Header Value is: [" + gatewayHeader + "]");
    	if (gatewayHeader == null || !gatewayHeader.contains("SecuredByGateway")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Direct access forbidden.");
        }
    	String userEmail = "system";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String[] chunks = token.split("\\.");
            if (chunks.length > 1) {
                String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));       
                System.out.println("DEBUG Azure Payload: " + payload);
                if (payload.contains("\"sub\"")) {
                    userEmail = payload.substring(payload.indexOf("\"sub\"") + 7, payload.indexOf("\"", payload.indexOf("\"sub\"") + 7));
                }
            }
        }            
       log.info("👤 Order processed for User Identity: [" + userEmail + "]");

        Inventory savedInventory = inventoryService.save(inventory);
        return ResponseEntity.ok(savedInventory);
    }
}
