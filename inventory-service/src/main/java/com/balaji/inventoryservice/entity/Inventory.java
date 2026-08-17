package com.balaji.inventoryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private long productId;
    private String sku;
    private Integer availableStock;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public Integer getAvailableStock() { return availableStock; }
    public void setAvailableStock(Integer stock) { this.availableStock = stock; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}
