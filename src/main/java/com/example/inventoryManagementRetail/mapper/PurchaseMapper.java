package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseResponseDto;
import com.example.inventoryManagementRetail.model.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    private final ProductMapper productMapper;
    private final SupplierMapper supplierMapper;

    public PurchaseMapper(ProductMapper productMapper, SupplierMapper supplierMapper) {
        this.productMapper = productMapper;
        this.supplierMapper = supplierMapper;
    }

    public Purchase convertDtoToEntity(PurchaseRequestDto purchaseRequestDto) {
        return Purchase.builder()
                .quantity(purchaseRequestDto.getQuantity())
                .amount(purchaseRequestDto.getAmount())
                .date(purchaseRequestDto.getDate())
                .build();
    }

    public PurchaseResponseDto convertEntityToDto(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .id(purchase.getId())
                .product(purchase.getProduct() != null
                        ? productMapper.convertToResponseDto(purchase.getProduct())
                        : null
                ).supplier(purchase.getSupplier() != null
                        ? supplierMapper.convertToResponseDto(purchase.getSupplier())
                        : null
                ).quantity(purchase.getQuantity())
                .amount(purchase.getAmount())
                .date(purchase.getDate())
                .build();
    }
}