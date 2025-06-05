package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseResponseDto;
import com.example.inventoryManagementRetail.model.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    private final ProductMapper productMapper;

    public PurchaseMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Purchase convertDtoToEntity(PurchaseRequestDto purchaseRequestDto) {
        return Purchase.builder()
                .product(purchaseRequestDto.getProduct())
                .supplier(purchaseRequestDto.getSupplier())
                .quantity(purchaseRequestDto.getQuantity())
                .amount(purchaseRequestDto.getAmount())
                .date(purchaseRequestDto.getDate())
                .build();
    }

    public PurchaseResponseDto convertEntityToDto(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .id(purchase.getId())
                .product(purchase.getProduct())
                .supplier(purchase.getSupplier())
                .quantity(purchase.getQuantity())
                .amount(purchase.getAmount())
                .date(purchase.getDate())
                .build();
    }
}