package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
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

    public ProductRequestDto convertToRequestDto(Product product) {
        return ProductRequestDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .buyPrice(product.getBuyPrice())
                .salePrice(product.getSalePrice())
                .stock(product.getStock())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .supplierId(product.getSupplier() != null ? product.getSupplier().getId() : null)
                .productTypeId(product.getProductType() != null ? product.getProductType().getId() : null)
                .build();
    }

    public ProductPatchRequestDto convertRequestDtoToPatchDto(ProductRequestDto productRequestDto) {
        return ProductPatchRequestDto.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .categoryId(productRequestDto.getCategoryId())
                .buyPrice(productRequestDto.getBuyPrice())
                .salePrice(productRequestDto.getSalePrice())
                .stock(productRequestDto.getStock())
                .supplierId(productRequestDto.getSupplierId())
                .productTypeId(productRequestDto.getProductTypeId())
                .build();
    }

    public ProductPatchRequestDto convertResponseDtoToPatchDto(ProductResponseDto productResponseDto) {
        return ProductPatchRequestDto.builder()
                .name(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .categoryId(productResponseDto.getCategoryId())
                .buyPrice(productResponseDto.getBuyPrice())
                .salePrice(productResponseDto.getSalePrice())
                .stock(productResponseDto.getStock())
                .supplierId(productResponseDto.getSupplierId())
                .productTypeId(productResponseDto.getProductTypeId())
                .build();
    }

    public Product convertResponseDtoToEntity(ProductResponseDto productResponseDto) {
        return Product.builder()
                .id(productResponseDto.getId())
                .name(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .category(Category.builder().id(productResponseDto.getCategoryId()).build())
                .buyPrice(productResponseDto.getBuyPrice())
                .salePrice(productResponseDto.getSalePrice())
                .stock(productResponseDto.getStock())
                .createdDate(productResponseDto.getCreatedDate())
                .updatedDate(productResponseDto.getUpdatedDate())
                .supplier(Supplier.builder().id(productResponseDto.getSupplierId()).build())
                .productType(ProductType.builder().id(productResponseDto.getProductTypeId()).build())
                .build();
    }
}