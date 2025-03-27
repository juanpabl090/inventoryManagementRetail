package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertToEntity(ProductRequestDto productRequestDto) {
        return Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .category(Category.builder().id(productRequestDto.getCategoryId()).build())
                .buyPrice(productRequestDto.getBuyPrice())
                .salePrice(productRequestDto.getSalePrice())
                .stock(productRequestDto.getStock())
                .createdDate(productRequestDto.getCreatedDate())
                .updatedDate(productRequestDto.getUpdatedDate())
                .supplier(Supplier.builder().id(productRequestDto.getSupplierId()).build())
                .productType(ProductType.builder().id(productRequestDto.getProductTypeId()).build())
                .build();
    }

    public ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .salePrice(product.getSalePrice())
                .buyPrice(product.getBuyPrice())
                .stock(product.getStock())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .supplierId(product.getSupplier() != null ? product.getSupplier().getId() : null)
                .productTypeId(product.getProductType() != null ? product.getProductType().getId() : null)
                .build();
    }
}