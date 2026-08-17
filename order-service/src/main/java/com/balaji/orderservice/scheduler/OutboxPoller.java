package com.balaji.orderservice.scheduler;

import com.balaji.orderservice.entity.Outbox;
import com.balaji.orderservice.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class OutboxPoller {

	private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);

	private final OutboxRepository outboxRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;

	// Constructor-based injection (replaces Lombok's @RequiredArgsConstructor)
	public OutboxPoller(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
		this.outboxRepository = outboxRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void pollOutboxAndPublish() {
		List<Outbox> unprocessedEvents = outboxRepository.findByProcessedFalseOrderByIdAsc();

		for (Outbox event : unprocessedEvents) {
			try {
				String topic;
				switch (event.getEventType()) {
				case "OrderCreated" -> topic = "order-events"; // Routed to Inventory Service

				case "OrderCompleted", "OrderRejected" -> topic = "order-status-events"; // Routed to Notification Service

				default -> {
					log.warn("Skipping unknown event type configuration: {}", event.getEventType());
					continue;
				}
				}

				kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
				.whenComplete((result, ex) -> {
					if (ex == null) {
						log.info("Successfully sent event to Kafka for Order ID: {}", event.getAggregateId());
					} else {
						log.error("Failed to send event to Kafka", ex);
					}
				});

				event.setProcessed(true);
				outboxRepository.save(event);

			} catch (Exception e) {
				log.error("Error processing outbox item ID: {}", event.getId(), e);
			}
		}
	}
}

