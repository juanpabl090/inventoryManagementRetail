package com.example.inventoryManagementRetail.dto.SupplierDto;

import com.example.inventoryManagementRetail.dto.ContactDto.ContactResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponseDto {
    private Long id;
    private String name;
    private ContactResponseDto contact;
}
