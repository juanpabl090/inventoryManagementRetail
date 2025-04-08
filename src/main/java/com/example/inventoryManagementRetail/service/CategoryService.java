package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.exception.DataPersistException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
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
            log.info("Category added successfully: id={}, name={}", categorySaved.getId(), categorySaved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.convertToDto(categorySaved));
        } catch (DuplicateResourceException e) {
            log.error("Duplicate category name detected: name={}", categoryRequestDto.getName(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error saving category: name={}, error={}", categoryRequestDto.getName(), e.getMessage(), e);
            throw new DataPersistException("An error occurred while saving the category");
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> updateCategoryById(Long id, CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
            log.info("Category updated successfully: id={}, newName={}", id, categoryRequestDto.getName());
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Error updating category by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating category");
        }
    }

    @Transactional
    public ResponseEntity<CategoryResponseDto> updateCategoryByName(String name, CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
            category.setName(categoryRequestDto.getName());
            categoryRepository.save(category);
            log.info("Category updated successfully: oldName={}, newName={}", name, categoryRequestDto.getName());
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Error updating category by name: oldName={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the category: " + name);
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteCategoryByid(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Category not found");
            }
            categoryRepository.deleteById(id);
            log.info("Category deleted successfully: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Category not found for deletion: id={}", id);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error deleting category by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting category");
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteCategoryByName(String name) {
        try {
            if (!categoryRepository.existsByName(name)) {
                throw new ResourceNotFoundException("Category not found");
            }
            categoryRepository.deleteByName(name);
            log.info("Category deleted successfully: name={}", name);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Category not found for deletion: name={}", name);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error deleting category by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting category");
        }
    }

    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                log.info("No categories found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            List<CategoryResponseDto> categoryResponseDtos = categories.stream().map(categoryMapper::convertToDto).toList();
            log.info("Categories retrieved successfully: count={}", categoryResponseDtos.size());
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDtos);
        } catch (DataAccessException e) {
            log.error("Error retrieving categories: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while retrieving category");
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryByName(String name) {
        try {
            Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
            log.info("Category retrieved successfully by name: name={}", name);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Error retrieving category by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while getting category by name: " + name);
        }
    }

    public ResponseEntity<CategoryResponseDto> getCategoryById(Long id) {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
            log.info("Category retrieved successfully by id: id={}", id);
            return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.convertToDto(category));
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting category by id: {}", id, e);
            throw new DataPersistException("An error occurred while getting category by id: " + id);
        }
    }

    /*public ResponseEntity<CategoryResponseDto> getCategoryByProduct(Long id) {
        return null;
    }*/
}