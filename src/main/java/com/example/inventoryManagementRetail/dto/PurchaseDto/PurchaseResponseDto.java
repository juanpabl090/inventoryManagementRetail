package com.example.inventoryManagementRetail.dto.PurchaseDto;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseResponseDto {
    private Long id;
    private ProductResponseDto product;
    private SupplierResponseDto supplier;
    private Long quantity;
    private BigDecimal amount;
    private LocalDateTime date;
}
