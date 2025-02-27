package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.crossstore.ChangeSetPersister;
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

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/allById/{id}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsById(@Valid @PathVariable Long id) {
        return productService.getAllProductsById(id);
    }

    @GetMapping("/allByName/{id}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByName(@Valid @PathVariable String name){
        return productService.getAllProductsByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.addProduct(productRequestDto);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@Valid @PathVariable Long id) {
        return productService.getProductByid(id);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@Valid @PathVariable String name) {
        return productService.getProductByName(name);
    }

    @DeleteMapping("/deleteByName/{name}")
    public ResponseEntity<?> deleteProductByName(@Valid @PathVariable String name) {
        return productService.deleteProductByName(name);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteProductById(@Valid @PathVariable Long id) {
        return productService.deleteProductById(id);
    }
}