package com.balaji.productservice.controller;

import com.balaji.productservice.dto.ProductExistsResponseDto;
import com.balaji.productservice.entity.Product;
import com.balaji.productservice.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
    	this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'PRODUCT_USER')")
    public ResponseEntity<?> createProduct(@RequestBody Product product, 
    		@RequestHeader(value = "X-Gateway-Validation", required = false) String gatewayHeader,
    		@RequestHeader(value = "Authorization", required = false) String authHeader) {
    	log.info("DEBUG: Received Gateway Header Value is: [" + gatewayHeader + "]");
    	if (gatewayHeader == null || !gatewayHeader.contains("SecuredByGateway")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Direct access forbidden.");
        }
    	
        Product createdProduct = productService.saveProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'PRODUCT_USER')")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'PRODUCT_USER')")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }
    
    @GetMapping("/{id}/exists")
    @PreAuthorize("hasAnyRole('ADMIN_USER', 'PRODUCT_USER')")
    public ProductExistsResponseDto checkProductExists(@PathVariable long id) {
    	log.info("Inside check product exists method for product id = {}", id);
        return productService.getById(id);
    }
}
