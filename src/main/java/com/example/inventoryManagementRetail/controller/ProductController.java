package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseWithDetailsDto;
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
        List<ProductResponseDto> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseWithDetailsDto>> getAllProductsWithDetails(){
        List<ProductResponseWithDetailsDto> productList = productService.getAllProductsWithDetails();
        return ResponseEntity.ok(productList);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto responseDto = productService.addProduct(productRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productById = productService.getProductById(id);
        return ResponseEntity.ok(productById);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ProductResponseDto> getProductByName(@PathVariable String name) {
        ProductResponseDto productByName = productService.getProductByName(name);
        return ResponseEntity.ok(productByName);
    }

    @GetMapping("/get/productTypeName/{productTypeName}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByProductType(@PathVariable String productTypeName) {
        List<ProductResponseDto> productsByProductType = productService.getProductsByProductType(productTypeName);
        return ResponseEntity.ok(productsByProductType);
    }

    @DeleteMapping("/delete/name/{name}")
    public void deleteProductByName(@PathVariable String name) {
        productService.deleteProductByName(name);
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping("/getByCategory/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDto> allProductsByCategory = productService.getAllProductsByCategory(categoryId);
        return ResponseEntity.ok(allProductsByCategory);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<ProductResponseDto> updateProductById(@PathVariable Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto responseDto = productService.updateProductById(id, productRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<ProductResponseDto> updateProductByName(@PathVariable String name, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto responseDto = productService.updateProductByName(name, productRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/update/name/{name}")
    public ResponseEntity<ProductResponseDto> updatePatchProductByName(@PathVariable String name, @RequestBody ProductPatchRequestDto productPatchRequestDto) {
        ProductResponseDto responseDto = productService.updatePatchProductByName(name, productPatchRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}