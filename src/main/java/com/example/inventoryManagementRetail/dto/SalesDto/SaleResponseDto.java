package com.example.inventoryManagementRetail.dto.SalesDto;

import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponseDto {
    private Long id;
    private LocalDateTime date;
    private BigDecimal amount;
    private List<SaleDetailsResponseDto> saleDetailsResponseDto;
}
