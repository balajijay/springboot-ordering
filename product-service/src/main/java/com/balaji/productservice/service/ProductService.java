package com.balaji.productservice.service;

import com.balaji.productservice.dto.ProductExistsResponseDto;
import com.balaji.productservice.entity.Product;
import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Product getProductBySku(String sku);
    ProductExistsResponseDto getById(long id);
}
