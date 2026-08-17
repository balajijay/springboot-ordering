package com.balaji.queueservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.balaji.queueservice.service.OrderQueueService;
import com.balaji.queueservice.entity.Order;
import com.balaji.queueservice.entity.OrderLineItem;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
	
	private final OrderQueueService orderQueueService;
	
	public QueueController (OrderQueueService orderQueueService) {
		this.orderQueueService = orderQueueService;
	}
	
	private static final Logger log = LoggerFactory.getLogger(QueueController.class);
	
	@GetMapping("/order/{id}")
	public Order getOrderCompleteInfo(@PathVariable long id) {
		return orderQueueService.getOrderCompleteInfo(id);
	}
	
	@PostMapping
	public List<OrderLineItem>  saveOrderData( @RequestBody String orderPayload ) throws Exception {
		log.info("Save Order Data");
		return orderQueueService.saveOrderData(orderPayload);
	}

}
