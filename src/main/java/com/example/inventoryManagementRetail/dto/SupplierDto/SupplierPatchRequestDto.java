package com.example.inventoryManagementRetail.dto.SupplierDto;

import com.example.inventoryManagementRetail.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SupplierPatchRequestDto {
    private String name;
    private Contact contact;
}
