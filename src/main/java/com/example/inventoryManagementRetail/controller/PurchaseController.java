package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseResponseDto;
import com.example.inventoryManagementRetail.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/registerPurchase")
    public ResponseEntity<PurchaseResponseDto> buy(@Valid @RequestBody PurchaseRequestDto purchaseRequestDto) {
        return purchaseService.registerPurchase(purchaseRequestDto);
    }

}