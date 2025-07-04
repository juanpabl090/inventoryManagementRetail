package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.model.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    // Convertir Sale a SaleResponseDto
    public SaleResponseDto saleEntityToSaleResponseDto(Sale sale) {
        return SaleResponseDto.builder()
                .id(sale.getId())
                .date(sale.getDate())
                .amount(sale.getAmount())
                .build();
    }
}
