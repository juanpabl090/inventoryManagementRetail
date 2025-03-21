package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.repository.SupplierRepository;
import com.example.inventoryManagementRetail.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public ResponseEntity<SupplierResponseDto> addSupplier(@Valid  SupplierRequestDto supplierRequestDto) {
        return SupplierService.addSupplier();
    }

    public ResponseEntity<SupplierResponseDto> updateSupplier(SupplierRequestDto supplierRequestDto) {
    }

    public ResponseEntity<SupplierResponseDto> deleteSupplier(SupplierRequestDto supplierRequestDto) {
    }

    public ResponseEntity<SupplierResponseDto> getallSupplier(SupplierRequestDto supplierRequestDto) {
    }

    public ResponseEntity<SupplierResponseDto> getByIdSupplier(SupplierRequestDto supplierRequestDto) {
    }

    public ResponseEntity<SupplierResponseDto> getByNameSupplier(SupplierRequestDto supplierRequestDto) {
    }

}