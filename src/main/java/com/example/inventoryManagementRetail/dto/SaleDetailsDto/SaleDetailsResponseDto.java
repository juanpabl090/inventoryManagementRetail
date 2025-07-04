package com.example.inventoryManagementRetail.dto.SaleDetailsDto;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailsResponseDto {
    private Long id;
    @JsonIgnore
    private SaleResponseDto saleResponseDto;
    private ProductPatchRequestDto productPatchRequestDtos;
    private BigDecimal amount;
    private BigDecimal discount;
    private Long quantity;
}