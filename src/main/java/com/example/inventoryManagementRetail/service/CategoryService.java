package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.exception.BadRequestException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.GenericException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.CategoryMapper;
import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CategoryService {

    //TODO: Continue with the implementation of the CategoryService class
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> addCategory(CategoryRequestDto categoryRequestDto) {
        Category category;
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new DuplicateResourceException("Category with the name '" + categoryRequestDto.getName() + "' is already exists");
        }
        try {
            category = categoryMapper.convertToEntity(categoryRequestDto);
            categoryRepository.save(category);
            log.info("Category added successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while saving the category: ", e);
            throw new GenericException("An error occurred while saving the category: " + categoryRequestDto.getName());
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> updateCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto == null) {
            throw new BadRequestException("Body of the request empty or invalid");
        }
        Category category = categoryRepository.findById(categoryRequestDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        try {
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
            log.info("Category updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating category", e);
            throw new GenericException("An error occurred while updating category");
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        try {
            categoryRepository.deleteById(id);
            log.info("Category delete successfully");
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting category", e);
            throw new GenericException("An error occurred while deleting category");
        }
    }

    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            List<CategoryResponseDto> categoryResponseDtos = categories.stream().map(categoryMapper::convertToDto).toList();
            log.info("Categories retrieved successfully");
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDtos);
        } catch (DataAccessException e) {
            log.error("Something went wrong while retrieved category", e);
            throw new GenericException("An error occurred while retrieving category");
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryByName(String name) {
        try {
            Category category = categoryRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
            log.info("Category got by name successfully");
            return ResponseEntity.status(HttpStatus.FOUND).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting category by name", e);
            throw new GenericException("An error occurred while getting category by name: " + name);
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryById(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found!"));
            log.info("Category got by id successfully");
            return ResponseEntity.status(HttpStatus.FOUND).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting category by id", e);
            throw new GenericException("An error occurred while getting category by id: " + id);
        }
    }

    /*public ResponseEntity<CategoryResponseDto> getCategoryByProduct(Long id) {
        return null;
    }*/
}