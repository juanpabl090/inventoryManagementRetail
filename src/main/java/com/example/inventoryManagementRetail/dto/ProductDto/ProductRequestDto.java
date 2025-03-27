package com.example.inventoryManagementRetail.dto.ProductDto;

import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.model.Supplier;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
public class ProductRequestDto {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 50, message = "Description must be between 1 and 50 characters")
    private String description;

    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotNull(message = "Buy price cannot be null")
    @Min(value = 0, message = "Buy price must be greater than 0")
    @PositiveOrZero(message = "Buy price must be positive")
    private BigDecimal buy_price;

    @NotNull(message = "Sale price cannot be null")
    @Min(value = 0, message = "Sale price must be greater than 0")
    @PositiveOrZero(message = "Sale price must be positive")
    private BigDecimal sale_price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must be greater than 0")
    @PositiveOrZero(message = "Stock must be positive")
    private Long stock;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;
    @NotNull(message = "Supplier cannot be null")
    private Supplier supplier;

    @NotNull(message = "Product type cannot be null")
    private ProductType product_type;
}