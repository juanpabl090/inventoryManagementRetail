package com.example.inventoryManagementRetail.dto.SaleDetailsDto;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailsRequestDto {
    private List<ProductPatchRequestDto> productsList;
    private BigDecimal discount; // porcentaje de descuento que se va a aplicar la venta
}