package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsRequestDto;
import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsResponseDto;
import com.example.inventoryManagementRetail.model.SaleDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleDetailsMapper {

    private final ProductMapper productMapper;
    private final SaleMapper saleMapper;

    public SaleDetailsMapper(ProductMapper productMapper, SaleMapper saleMapper) {
        this.productMapper = productMapper;
        this.saleMapper = saleMapper;
    }

    // Convertir SaleDetailsRequestDto a SaleDetails
    public SaleDetails saleDetailsRequestDtoToSaleDetailsEntity(SaleDetailsRequestDto saleDetailsRequestDto) {
        return SaleDetails.builder()
                .product(productMapper.convertPatchDtoToEntity((ProductPatchRequestDto) saleDetailsRequestDto.getProductsList())) // Asegúrate de que esto esté implementado
                .amount(saleDetailsRequestDto.getAmount())
                .discount(saleDetailsRequestDto.getDiscount())
                .quantity(saleDetailsRequestDto.getQuantity())
                .build();
    }

    // Convertir SaleDetails a SaleDetailsResponseDto
    public SaleDetailsResponseDto saleDetailsEntityToResponseDto(SaleDetails saleDetails) {
        return SaleDetailsResponseDto.builder()
                .id(saleDetails.getId())
                .saleResponseDto(saleMapper.saleEntityToSaleResponseDto(saleDetails.getSale()))
                .productPatchRequestDtos(productMapper.convertEntityToPatch(saleDetails.getProduct())) // Asegúrate de que esto esté implementado
                .amount(saleDetails.getAmount())
                .discount(saleDetails.getDiscount())
                .quantity(saleDetails.getQuantity())
                .build();
    }

    public List<SaleDetailsResponseDto> convertSaleDetailsListToSaleDetailsResponseDtoList(List<SaleDetails> saleDetails) {
        List<SaleDetailsResponseDto> saleDetailsResponseDtoList = new ArrayList<>();
        for (int i = 0; i < saleDetails.size(); i++) {
            saleDetailsResponseDtoList.add(SaleDetailsResponseDto.builder()
                    .id(saleDetails.get(i).getId())
                    .saleResponseDto(saleMapper.saleEntityToSaleResponseDto(saleDetails.get(i).getSale()))
                    .productPatchRequestDtos(productMapper.convertEntityToPatch(saleDetails.get(i).getProduct()))
                    .amount(saleDetails.get(i).getAmount())
                    .discount(saleDetails.get(i).getDiscount())
                    .quantity(saleDetails.get(i).getQuantity())
                    .build());

        }
        return saleDetailsResponseDtoList;
    }
}
