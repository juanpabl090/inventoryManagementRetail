package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
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

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> addCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new DuplicateResourceException("Category with the name '" + categoryRequestDto.getName() + "' is already exists");
        }
        try {
            Category category = categoryMapper.convertToEntity(categoryRequestDto);
            Category categorySaved = categoryRepository.save(category);
            log.info("Category added successfully: {}", categorySaved);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.convertToDto(categorySaved));
        } catch (DuplicateResourceException e) {
            log.error("Something went wrong while checking if category exists by name: {}", categoryRequestDto.getName());
            throw e;
        } catch (DataAccessException e) {
            log.error("Something went wrong while saving the category: {}: ", categoryRequestDto, e);
            throw new RuntimeException("An error occurred while saving the category", e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> updateCategoryById(Long id, CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
            log.info("Category updated by id successfully: {}", category);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating the product by id: {}", categoryRequestDto != null ? categoryRequestDto : "unknown", e);
            throw new GenericException("An error occurred while updating category");
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> updateCategoryByName(String name, CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
            log.info("Category updated by name successfully: {}", categoryRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.info("updateSupplierByName: Something went wrong while updating the product by name: {}", categoryRequestDto != null ? categoryRequestDto : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (categoryRequestDto != null ? categoryRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteCategoryByid(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Category not found");
            }
            categoryRepository.deleteById(id);
            log.info("Category delete By id successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting category by id: {}", id, e);
            throw new GenericException("An error occurred while deleting category");
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteCategoryByName(String name) {
        try {
            if (!categoryRepository.existsByName(name)) {
                log.info("Category does not exist");
                throw new ResourceNotFoundException("Category not found");
            }
            categoryRepository.deleteByName(name);
            log.info("Category delete by name successfully: {}", name);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.info("Something went wrong while deleting category by name: {}", name, e);
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
            log.info("Categories retrieved successfully: {}", categoryResponseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDtos);
        } catch (DataAccessException e) {
            log.error("Something went wrong while retrieved category", e);
            throw new GenericException("An error occurred while retrieving category");
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryByName(String name) {
        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
            log.info("Category got by name successfully: {}", category);
            return ResponseEntity.status(HttpStatus.FOUND).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting category by name: {}", name, e);
            throw new GenericException("An error occurred while getting category by name: " + name);
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryById(Long id) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
            log.info("Category got by id successfully: {}", category);
            return ResponseEntity.status(HttpStatus.FOUND).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting category by id: {}", id, e);
            throw new GenericException("An error occurred while getting category by id: " + id);
        }
    }

    /*public ResponseEntity<CategoryResponseDto> getCategoryByProduct(Long id) {
        return null;
    }*/
}