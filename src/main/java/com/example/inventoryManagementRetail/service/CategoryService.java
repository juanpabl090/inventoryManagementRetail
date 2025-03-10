package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.exception.BadRequestException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.CategoryMapper;
import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.repository.CategoryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.ErrorManager;

@Service
public class CategoryService {

    //TODO: Continue with the implementation of the CategoryService class
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public ResponseEntity<CategoryResponseDto> addCategory(CategoryRequestDto categoryRequestDto) {
        Category category;
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new DuplicateResourceException("Category with the name '" + categoryRequestDto.getName() + "' is already exists");
        }
        try {
            category = categoryMapper.convertToEntity(categoryRequestDto);
            categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the category", e.getCause());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.convertToDto(category));
    }

    public ResponseEntity<?> updateCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto == null) {
            throw new BadRequestException("Body of the request empty or invalid");
        }
        Category category = categoryRepository.findById(categoryRequestDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        try {
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
        } catch (DataAccessException e) {
            throw new RuntimeException("An error occurred while updating the category", e.getCause());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<CategoryResponseDto> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataAccessException e) {
            //TODO: agregar logger
            throw new RuntimeException("An error occurred while deleting the category", e.getCause());
        }
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<CategoryResponseDto> getAllCategories() {
        return null;
    }

    public ResponseEntity<CategoryResponseDto> getCategoryByName(String name) {
        return null;
    }

    public ResponseEntity<CategoryResponseDto> getCategoryById(Long id) {
        return null;
    }

    /*public ResponseEntity<CategoryResponseDto> getCategoryByProduct(Long id) {
        return null;
    }*/
}