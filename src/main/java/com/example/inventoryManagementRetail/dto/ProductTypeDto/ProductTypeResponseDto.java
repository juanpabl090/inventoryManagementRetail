package com.example.inventoryManagementRetail.dto.ProductTypeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeResponseDto {
    private Long id;
    private String name;
}