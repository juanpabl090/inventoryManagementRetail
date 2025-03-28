package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeRequestDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.service.ProductTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProductType")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProductTypeResponseDto> addProductType(@Valid @RequestBody ProductTypeRequestDto productTypeRequestDto) {
        return productTypeService.addProductType(productTypeRequestDto);
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductTypeResponseDto>> getAllProductType() {
        return productTypeService.getAllProductType();
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ProductTypeResponseDto> getProductTypeById(@PathVariable Long id) {
        return productTypeService.getProductTypeById(id);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ProductTypeResponseDto> getProductTypeByName(@PathVariable String name) {
        return productTypeService.getProductTypeByName(name);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteProductTypeById(@PathVariable Long id) {
        return productTypeService.deleteProductTypeById(id);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<?> deleteProductTypeByName(@PathVariable String name) {
        return productTypeService.deleteProductTypeByName(name);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<ProductTypeResponseDto> updateProductTypeById(@PathVariable Long id, @Valid @RequestBody ProductTypeRequestDto productTypeRequestDto) {
        return productTypeService.updateProductTypeById(id, productTypeRequestDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<ProductTypeResponseDto> updateProductTypeByName(@PathVariable String name, @Valid @RequestBody ProductTypeRequestDto productTypeRequestDto) {
        return productTypeService.updateProductTypeByName(name, productTypeRequestDto);
    }
}