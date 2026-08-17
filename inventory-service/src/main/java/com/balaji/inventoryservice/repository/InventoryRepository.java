package com.balaji.inventoryservice.repository;

import com.balaji.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(long productId);
    Inventory findBySku(String sku);
}
