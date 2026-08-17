package com.balaji.productservice.service;

import com.balaji.productservice.entity.Product;
import com.balaji.productservice.event.ProductCreatedEvent;
import com.balaji.productservice.repository.ProductRepository;
import com.balaji.productservice.dto.ProductExistsResponseDto;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public ProductServiceImpl(ProductRepository productRepository, KafkaTemplate<String, Object> kafkaTemplate ) {
    	this.productRepository = productRepository;
    	this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional // Ensures database data safety across threads
    public Product saveProduct(Product product) {
        log.info("💾 Committing new catalog entry to productdb H2 instance for SKU: {}", product.getSku());
        Product savedProduct = productRepository.save(product);

        // Map data directly to your Kafka event contract wrapper
        ProductCreatedEvent event = new ProductCreatedEvent(
            savedProduct.getSku(),
            savedProduct.getId(),
            savedProduct.getPrice()
        );

        log.info("🚀 Broadcasting product-catalog-events notice to Kafka cluster for SKU: {}", savedProduct.getSku());
        // Stream it across your KRaft cluster partitions using SKU as the route key
        kafkaTemplate.send("product-catalog-events", savedProduct.getSku(), event);

        return savedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Product catalog entry not found for SKU: " + sku));
    }

	@Override
	public ProductExistsResponseDto getById(long id) {
		return new ProductExistsResponseDto((productRepository.findById(id)).isPresent(), id);
	}
}
