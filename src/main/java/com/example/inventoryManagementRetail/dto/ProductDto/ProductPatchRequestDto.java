package com.example.inventoryManagementRetail.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPatchRequestDto {
    private String name;
    private String description;
    private Long categoryId;
    private BigDecimal buyPrice;
    private BigDecimal salePrice;
    private Long stock;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long supplierId;
    private Long productTypeId;
}
