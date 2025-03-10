package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryRequestDto;
import com.example.inventoryManagementRetail.dto.CategoryDto.CategoryResponseDto;
import com.example.inventoryManagementRetail.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto convertToDto(Category category) {
        return CategoryResponseDto.builder()
                .name(category.getName())
                .build();
    }

    public Category convertToEntity(CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .id(categoryRequestDto.getId())
                .name(categoryRequestDto.getName())
                .build();
    }
}
