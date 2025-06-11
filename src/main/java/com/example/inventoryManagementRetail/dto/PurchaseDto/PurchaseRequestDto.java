package com.example.inventoryManagementRetail.dto.PurchaseDto;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class PurchaseRequestDto {
    @NotNull(message = "Product cannot be null")
    private ProductRequestDto product;
    @NotNull(message = "Supplier cannot be null")
    private SupplierRequestDto supplier;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Long quantity;
    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    @PositiveOrZero(message = "Amount must be positive or zero")
    private BigDecimal amount;
    private LocalDateTime date;
}