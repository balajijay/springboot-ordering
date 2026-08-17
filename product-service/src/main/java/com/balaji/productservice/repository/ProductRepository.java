package com.balaji.productservice.repository;

import com.balaji.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Allows searching products by their unique Stock Keeping Unit string
    Optional<Product> findBySku(String sku);
}
