package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.service.CategoryService;
import jakarta.transaction.Transactional;
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
        return categoryService.addCategory(categoryRequestDto);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategoryById(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategoryById(id, categoryRequestDto);
    }

    @PutMapping("/update/name/{name}")
    public ResponseEntity<CategoryResponseDto> updateCategoryByName(@PathVariable String name, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategoryByName(name, categoryRequestDto);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteCategoryByid(@PathVariable Long id) {
        return categoryService.deleteCategoryByid(id);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<Void> deleteCategoryByName(@PathVariable String name) {
        return categoryService.deleteCategoryByName(name);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}