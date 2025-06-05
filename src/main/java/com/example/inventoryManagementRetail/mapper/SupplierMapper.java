package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    private final ContactMapper contactMapper;

    public SupplierMapper(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    /**
     * Convert Supplier Entity to Supplier Response DTO
     *
     * @param supplier
     * @return
     */
    public SupplierResponseDto convertToResponseDto(Supplier supplier) {
        return SupplierResponseDto.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contact(contactMapper.convertToContactResponseDto(supplier.getContact()))
                .build();
    }

    /**
     * Convert Suplier request DTO to Supplier Entity
     *
     * @param supplierRequestDto
     * @return
     */
    public Supplier convertToEntity(SupplierRequestDto supplierRequestDto) {
        return Supplier.builder()
                .name(supplierRequestDto.getName())
                .contact(contactMapper.convertToContactEntity(supplierRequestDto.getContact()))
                .build();
    }

    public SupplierRequestDto convertToRequestDto(Supplier supplier) {
        return SupplierRequestDto.builder()
                .name(supplier.getName())
                .contact(contactMapper.convertToContactEntity(supplier.getContact()))
                .build();
    }
}