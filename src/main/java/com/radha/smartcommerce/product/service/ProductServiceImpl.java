package com.radha.smartcommerce.product.service;

import com.radha.smartcommerce.exception.ResourceNotFoundException;
import com.radha.smartcommerce.product.dto.CreateProductRequest;
import com.radha.smartcommerce.product.dto.ProductResponse;
import com.radha.smartcommerce.product.dto.UpdateProductRequest;
import com.radha.smartcommerce.product.entity.Category;
import com.radha.smartcommerce.product.entity.Product;
import com.radha.smartcommerce.product.mapper.ProductMapper;
import com.radha.smartcommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toProduct(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductResponse> responses = products.map(productMapper::toProductResponse);
        return responses;
    }

    @Override
    public Page<ProductResponse> getProductByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByCategory(category, pageable);
        Page<ProductResponse> responses = products.map(productMapper::toProductResponse);
        return responses;
    }

    @Override
    public Page<ProductResponse> searchProductsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<ProductResponse> responses = products.map(productMapper::toProductResponse);
        return responses;
    }

    @Override
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
        product.setStockQuantity(request.stockQuantity());
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        boolean exists = productRepository.existsById(productId);
        if (exists) productRepository.deleteById(productId);
        else throw new ResourceNotFoundException("Product Not Found");
    }
}
