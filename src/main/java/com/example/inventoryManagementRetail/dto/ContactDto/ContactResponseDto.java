package com.example.inventoryManagementRetail.dto.ContactDto;

import com.example.inventoryManagementRetail.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDto {
    private Long id;
    private String phone;
    private String email;
    private String address;
}
