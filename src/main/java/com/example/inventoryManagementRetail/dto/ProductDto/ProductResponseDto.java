package com.example.inventoryManagementRetail.dto.ProductDto;

import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.model.Supplier;
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
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private BigDecimal buy_price;
    private BigDecimal sale_price;
    private Long stock;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;
    private Supplier supplier;
}
