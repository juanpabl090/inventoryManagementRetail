package com.example.inventoryManagementRetail.mapper;

import com.example.inventoryManagementRetail.dto.ContactDto.ContactRequestDto;
import com.example.inventoryManagementRetail.dto.ContactDto.ContactResponseDto;
import com.example.inventoryManagementRetail.model.Contact;
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
     * @param contact
     * @return
     */
    public Contact convertToContactEntity(Contact contact) {
        return Contact.builder()
                .address(contact.getAddress())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
