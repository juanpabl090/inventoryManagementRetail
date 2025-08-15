package com.example.inventoryManagementRetail.dto.ProductDto;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductResponseWithDetailsDto {
    private Long id;
    private String name;
    private String description;
    private CategoryResponseDto category;
    private BigDecimal buyPrice;
    private BigDecimal salePrice;
    private Long stock;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private SupplierResponseDto supplier;
    private ProductTypeResponseDto productType;
}