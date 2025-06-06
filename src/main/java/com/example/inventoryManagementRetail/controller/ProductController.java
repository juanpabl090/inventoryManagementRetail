package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.addProduct(productRequestDto);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("/get/productTypeName/{productTypeName}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByProductType(@PathVariable String productTypeName) {
        return productService.getProductsByProductType(productTypeName);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<?> deleteProductByName(@PathVariable String name) {
        return productService.deleteProductByName(name);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    @GetMapping("/getByCategory/{categoryId}")
    public ResponseEntity<?> getAllProductsByCategory(@PathVariable Long categoryId) {
        return productService.getAllProductsByCategory(categoryId);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<ProductResponseDto> updateProductById(@PathVariable Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProductById(id, productRequestDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<ProductResponseDto> updateProductByName(@PathVariable String name, @Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProductByName(name, productRequestDto);
    }

    @PatchMapping("/update/name/{name}")
    public ResponseEntity<ProductResponseDto> updatePatchProductByName(@PathVariable String name, @RequestBody ProductPatchRequestDto productPatchRequestDto) {
        return productService.updatePatchProductByName(name, productPatchRequestDto);
    }
}