package com.balaji.productservice.event;

import java.math.BigDecimal;

public class ProductCreatedEvent {
    private String sku;
    private long productId;
    private BigDecimal price;

    public ProductCreatedEvent() {}
    
    public ProductCreatedEvent(String sku, long productId, BigDecimal price) {
        this.sku = sku;
        this.productId = productId;
        this.price = price;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

