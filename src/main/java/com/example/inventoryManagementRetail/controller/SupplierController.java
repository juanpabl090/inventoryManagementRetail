package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/add")
    public ResponseEntity<SupplierResponseDto> addSupplier(@Valid @RequestBody SupplierRequestDto supplierRequestDto) {
        return supplierService.addSupplier(supplierRequestDto);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplierById(@PathVariable Long id, @Valid @RequestBody SupplierRequestDto supplierRequestDto) {
        return supplierService.updateSupplierById(id, supplierRequestDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<SupplierResponseDto> updateSupplierByName(@PathVariable String name, @Valid @RequestBody SupplierRequestDto supplierRequestDto) {
        return supplierService.updateSupplierByName(name, supplierRequestDto);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable Long id) {
        return supplierService.deleteSupplierById(id);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<Void> deleteSupplierByName(@PathVariable String name) {
        return supplierService.deleteSupplierByName(name);
    }

    @GetMapping("/get")
    public ResponseEntity<List<SupplierResponseDto>> getAllSupplier() {
        return supplierService.getAllSupplier();
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<SupplierResponseDto> getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<SupplierResponseDto> getSupplierByName(@PathVariable String name) {
        return supplierService.getSupplierByName(name);
    }

}