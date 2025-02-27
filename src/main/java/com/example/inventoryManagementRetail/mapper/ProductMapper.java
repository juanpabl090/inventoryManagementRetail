package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertToEntity(ProductRequestDto productRequestDto) {
        return Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .category(productRequestDto.getCategory())
                .buy_price(productRequestDto.getBuy_price())
                .sale_price(productRequestDto.getSale_price())
                .stock(productRequestDto.getStock())
                .created_date(productRequestDto.getCreated_date())
                .updated_date(productRequestDto.getUpdated_date())
                .supplier(productRequestDto.getSupplier())
                .product_type(productRequestDto.getProduct_type())
                .build();
    }

    public ProductResponseDto convertToResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .buy_price(product.getBuy_price())
                .sale_price(product.getSale_price())
                .stock(product.getStock())
                .created_date(product.getCreated_date())
                .updated_date(product.getUpdated_date())
                .supplier(product.getSupplier())
                .build();
    }
}