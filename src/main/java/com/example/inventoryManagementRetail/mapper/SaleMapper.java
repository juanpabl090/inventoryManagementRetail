package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsResponseDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.model.Sale;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SaleMapper {
    private final SaleDetailsMapper saleDetailsMapper;

    public SaleMapper(@Lazy SaleDetailsMapper saleDetailsMapper) {
        this.saleDetailsMapper = saleDetailsMapper;
    }

    // Convertir Sale a SaleResponseDto
    public SaleResponseDto saleEntityToSaleResponseDto(Sale sale) {
        if (sale == null) return null;

        List<SaleDetailsResponseDto> details = sale.getSaleDetails() == null
                ? Collections.emptyList()
                : sale.getSaleDetails()
                .stream()
                .map(saleDetailsMapper::saleDetailsEntityToResponseDto)
                .toList();

        return SaleResponseDto.builder()
                .id(sale.getId())
                .date(sale.getDate())
                .amount(sale.getAmount())
                .saleDetailsResponseDto(details)
                .build();
    }
}
