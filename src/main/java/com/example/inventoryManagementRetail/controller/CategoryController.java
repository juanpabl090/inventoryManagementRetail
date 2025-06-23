package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDto> addCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto response = categoryService.addCategory(categoryRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto response = categoryService.updateCategoryById(id, categoryRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<CategoryResponseDto> updateCategoryByName(@PathVariable String name, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto response = categoryService.updateCategoryByName(name, categoryRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteCategoryByid(@PathVariable Long id) {
        categoryService.deleteCategoryByid(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<Void> deleteCategoryByName(@PathVariable String name) {
        categoryService.deleteCategoryByName(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        CategoryResponseDto response = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }
}