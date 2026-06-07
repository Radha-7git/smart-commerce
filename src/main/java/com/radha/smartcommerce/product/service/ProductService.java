package com.radha.smartcommerce.product.service;

import com.radha.smartcommerce.product.dto.CreateProductRequest;
import com.radha.smartcommerce.product.dto.ProductResponse;
import com.radha.smartcommerce.product.dto.UpdateProductRequest;
import com.radha.smartcommerce.product.entity.Category;
import org.springframework.data.domain.Page;


public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(Long productId);
    Page<ProductResponse> getAllProducts(int page, int size);
    Page<ProductResponse> getProductByCategory(Category category, int page, int size);
    Page<ProductResponse> searchProductsByName (String name, int page, int size);
    ProductResponse updateProduct(Long productId, UpdateProductRequest request);
    void deleteProduct(Long productId);
}
