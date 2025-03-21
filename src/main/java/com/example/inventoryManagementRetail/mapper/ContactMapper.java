package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ContactDto.ContactResponseDto;
import com.example.inventoryManagementRetail.model.Contact;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    /**
     * Convert Contact Entity to Contact Response DTO
     *
     * @param contact
     * @return
     */
    public ContactResponseDto convertToContactResponseDto(Contact contact) {
        return ContactResponseDto.builder()
                .id(contact.getId())
                .address(contact.getAddress())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    /**
     * Convert Contact Response DTO to Contact Entity
     *
     * @param contactRequestDto
     * @return
     */
    public Contact convertToContactEntity(Contact contactRequestDto) {
        return Contact.builder()
                .address(contactRequestDto.getAddress())
                .email(contactRequestDto.getEmail())
                .phone(contactRequestDto.getPhone())
                .build();
    }
}
