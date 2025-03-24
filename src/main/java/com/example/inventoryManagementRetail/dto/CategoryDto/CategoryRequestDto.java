package com.example.inventoryManagementRetail.dto.CategoryDto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDto {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 50, message = "Name category must be between 1 and 50 characters")
    private String name;
}
