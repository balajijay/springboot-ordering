package com.balaji.queueservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.balaji.queueservice.entity.*;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

	List<OrderLineItem> findByOrderId(Long orderId);
	
}
