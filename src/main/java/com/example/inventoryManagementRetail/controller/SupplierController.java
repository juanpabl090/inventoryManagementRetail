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
        SupplierResponseDto supplierResponseDto = supplierService.addSupplier(supplierRequestDto);
        return ResponseEntity.ok(supplierResponseDto);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplierById(@PathVariable Long id, @Valid @RequestBody SupplierRequestDto supplierRequestDto) {
        SupplierResponseDto supplierResponseDto = supplierService.updateSupplierById(id, supplierRequestDto);
        return ResponseEntity.ok(supplierResponseDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<SupplierResponseDto> updateSupplierByName(@PathVariable String name, @Valid @RequestBody SupplierRequestDto supplierRequestDto) {
        SupplierResponseDto supplierResponseDto = supplierService.updateSupplierByName(name, supplierRequestDto);
        return ResponseEntity.ok(supplierResponseDto);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteSupplierById(@PathVariable Long id) {
        supplierService.deleteSupplierById(id);
    }

    @DeleteMapping("/delete/name/{name}")
    public void deleteSupplierByName(@PathVariable String name) {
        supplierService.deleteSupplierByName(name);
    }

    @GetMapping("/get")
    public ResponseEntity<List<SupplierResponseDto>> getAllSupplier() {
        List<SupplierResponseDto> allSupplier = supplierService.getAllSupplier();
        return ResponseEntity.ok(allSupplier);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<SupplierResponseDto> getSupplierById(@PathVariable Long id) {
        SupplierResponseDto supplierById = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplierById);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<SupplierResponseDto> getSupplierByName(@PathVariable String name) {
        SupplierResponseDto supplierByName = supplierService.getSupplierByName(name);
        return ResponseEntity.ok(supplierByName);
    }

}