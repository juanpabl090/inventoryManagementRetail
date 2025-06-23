package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeRequestDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
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
        ProductTypeResponseDto productTypeResponseDto = productTypeService.addProductType(productTypeRequestDto);
        return ResponseEntity.ok(productTypeResponseDto);
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductTypeResponseDto>> getAllProductType() {
        List<ProductTypeResponseDto> allProductType = productTypeService.getAllProductType();
        return ResponseEntity.ok(allProductType);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ProductTypeResponseDto> getProductTypeById(@PathVariable Long id) {
        ProductTypeResponseDto productTypeById = productTypeService.getProductTypeById(id);
        return ResponseEntity.ok(productTypeById);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ProductTypeResponseDto> getProductTypeByName(@PathVariable String name) {
        ProductTypeResponseDto productTypeByName = productTypeService.getProductTypeByName(name);
        return ResponseEntity.ok(productTypeByName);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteProductTypeById(@PathVariable Long id) {
        productTypeService.deleteProductTypeById(id);
    }

    @DeleteMapping("/delete/name/{name}")
    public void deleteProductTypeByName(@PathVariable String name) {
        productTypeService.deleteProductTypeByName(name);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<ProductTypeResponseDto> updateProductTypeById(@PathVariable Long id, @Valid @RequestBody ProductTypeRequestDto productTypeRequestDto) {
        ProductTypeResponseDto productTypeResponseDto = productTypeService.updateProductTypeById(id, productTypeRequestDto);
        return ResponseEntity.ok(productTypeResponseDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<ProductTypeResponseDto> updateProductTypeByName(@PathVariable String name, @Valid @RequestBody ProductTypeRequestDto productTypeRequestDto) {
        ProductTypeResponseDto productTypeResponseDto = productTypeService.updateProductTypeByName(name, productTypeRequestDto);
        return ResponseEntity.ok(productTypeResponseDto);
    }
}