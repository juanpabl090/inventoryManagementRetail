package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeRequestDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
import com.example.inventoryManagementRetail.model.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper {
    //convertDtoToEntity
    public ProductType convertDtoToEntity(ProductTypeRequestDto productTypeRequestDto) {
        return ProductType.builder()
                .name(productTypeRequestDto.getName())
                .build();
    }

    //convertEntityToDto
    public ProductTypeResponseDto convertEntityToDto(ProductType productType) {
        return ProductTypeResponseDto.builder()
                .id(productType.getId())
                .name(productType.getName())
                .build();
    }

}