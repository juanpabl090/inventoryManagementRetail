package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsRequestDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/register")
    public ResponseEntity<SaleResponseDto> sale(@Valid @RequestBody SaleDetailsRequestDto saleDetailsRequestDto) {
        SaleResponseDto saleResponseDto = saleService.registerSale(saleDetailsRequestDto);
        return ResponseEntity.ok(saleResponseDto);
    }
}