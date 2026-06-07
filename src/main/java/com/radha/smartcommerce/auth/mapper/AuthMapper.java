package com.radha.smartcommerce.auth.mapper;

import com.radha.smartcommerce.auth.dto.RegisterRequest;
import com.radha.smartcommerce.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target="id", ignore= true)
    @Mapping(target="role", expression = "java(com.radha.smartcommerce.auth.entity.Role.CUSTOMER)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequest request);
}
