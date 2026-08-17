package com.balaji.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long productId;
    private Integer quantity;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; 
    
 // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public OrderStatus getStatus() { return status;   }
    public void setStatus(OrderStatus status) {     	this.status = status;  }
    
    
}
