package com.example.inventoryManagementRetail.dto.SupplierDto;

import com.example.inventoryManagementRetail.model.Contact;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequestDto {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name cannot be null")
    private String name;
    @NotNull(message = "Contact cannot be null")
    private Contact contact;
}