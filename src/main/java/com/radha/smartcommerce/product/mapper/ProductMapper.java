package com.radha.smartcommerce.product.mapper;

import com.radha.smartcommerce.product.dto.CreateProductRequest;
import com.radha.smartcommerce.product.dto.ProductResponse;
import com.radha.smartcommerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(CreateProductRequest request);
    ProductResponse toProductResponse(Product product);
}
