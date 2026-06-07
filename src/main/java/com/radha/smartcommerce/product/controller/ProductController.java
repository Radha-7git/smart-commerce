package com.radha.smartcommerce.product.controller;

import com.radha.smartcommerce.common.response.ApiResponse;
import com.radha.smartcommerce.product.dto.CreateProductRequest;
import com.radha.smartcommerce.product.dto.ProductResponse;
import com.radha.smartcommerce.product.dto.UpdateProductRequest;
import com.radha.smartcommerce.product.entity.Category;
import com.radha.smartcommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse productResponse = productService.createProduct(request);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product created successfully")
                .data(productResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById (@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(productResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping()
    public ApiResponse<Page<ProductResponse>> geetAllProducts(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponse> productResponses = productService.getAllProducts(page, size);
        return ApiResponse.<Page<ProductResponse>>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(productResponses)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/category/{category}")
    public ApiResponse<Page<ProductResponse>> getProductByCategory (
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        Page<ProductResponse> productResponses = productService.getProductByCategory(category, page, size);
        return ApiResponse.<Page<ProductResponse>>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(productResponses)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<Page<ProductResponse>> searchProductsName (
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponse> productResponses = productService.searchProductsByName(name, page, size);
        return ApiResponse.<Page<ProductResponse>>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(productResponses)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @PutMapping("{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> updateProduct (@PathVariable Long productId, @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse productResponse = productService.updateProduct(productId, request);
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product updated successfully")
                .data(productResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteProduct (@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
