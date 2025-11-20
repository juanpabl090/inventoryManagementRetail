package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsResponseDto;
import com.example.inventoryManagementRetail.model.SaleDetails;
import org.springframework.stereotype.Component;

@Component
public class SaleDetailsMapper {

    private final ProductMapper productMapper;
    private final SaleMapper saleMapper;

    public SaleDetailsMapper(ProductMapper productMapper, SaleMapper saleMapper) {
        this.productMapper = productMapper;
        this.saleMapper = saleMapper;
    }

    public SaleDetailsResponseDto saleDetailsEntityToResponseDto(SaleDetails saleDetails) {
        return SaleDetailsResponseDto.builder()
                .id(saleDetails.getId())
                .product(productMapper.convertResponseDtoToPatchDto(productMapper.convertToResponseDto(saleDetails.getProduct())))
                .amount(saleDetails.getAmount())
                .discount(saleDetails.getDiscount())
                .quantity(saleDetails.getQuantity())
                .build();
    }
}
