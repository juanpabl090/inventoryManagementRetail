package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto convertToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category convertToEntity(CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getName())
                .build();
    }
}
