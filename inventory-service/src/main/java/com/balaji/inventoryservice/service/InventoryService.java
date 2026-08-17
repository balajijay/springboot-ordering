package com.balaji.inventoryservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.balaji.inventoryservice.entity.Inventory;
import com.balaji.inventoryservice.repository.InventoryRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryService {
	private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
	
	public final InventoryRepository inventoryRepository;
	
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
	
	public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
	
	@Transactional
	public Inventory save(Inventory inventory) {
		log.info("Save Inventory beginning");
		if (inventory.getAvailableStock() < 0) return inventory;
		Inventory existingInventory = inventoryRepository.findBySku(inventory.getSku());
		if (existingInventory != null) {
			log.info("Update Inventory for sku = {}", existingInventory.getSku());
			Integer availableQuantity = existingInventory.getAvailableStock();
			existingInventory.setAvailableStock(availableQuantity + inventory.getAvailableStock());
			return inventoryRepository.save(existingInventory);
		}
		log.info("Insert Inventory");
		return inventoryRepository.save(inventory);
		
	}
	
}
