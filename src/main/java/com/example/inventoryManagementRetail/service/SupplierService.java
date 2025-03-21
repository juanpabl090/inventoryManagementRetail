package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.SupplierMapper;
import com.example.inventoryManagementRetail.model.Contact;
import com.example.inventoryManagementRetail.model.Supplier;
import com.example.inventoryManagementRetail.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> addSupplier(SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierMapper.convertToEntity(supplierRequestDto);
            Contact contact = new Contact();

            contact.setAddress(supplierRequestDto.getContact().getAddress());
            contact.setEmail(supplierRequestDto.getContact().getEmail());
            contact.setPhone(supplierRequestDto.getContact().getPhone());

            supplier.setContact(contact);
            supplierRepository.save(supplier);

            log.info("addSupplier: Supplier and its contact added successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(supplierMapper.convertToResponseDto(supplier));
        } catch (DataAccessException e) {
            log.info("addSupplier: Something went wrong while saving the product", e);
            throw new RuntimeException("An error occurred while saving the product", e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updateSupplierById(Long id, SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierRepository.findById(id).orElseGet(() -> {
                log.info("updateSupplierById: supplier with id: {} not found", id);
                throw new ResourceNotFoundException("Supplier with id: " + id + " not found");
            });
            Contact contact = Contact.builder()
                    .phone(supplierRequestDto.getContact().getPhone())
                    .email(supplierRequestDto.getContact().getEmail())
                    .address(supplierRequestDto.getContact().getAddress())
                    .build();
            supplier.setName(supplierRequestDto.getName());
            supplier.setContact(contact);
            log.info("updateSupplierById: Supplier updated successfully!");
            supplierRepository.save(supplier);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
        } catch (DataAccessException e) {
            log.info("updateSupplierById: Something went wrong while updating the product: {}", supplierRequestDto != null ? supplierRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (supplierRequestDto != null ? supplierRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updateSupplierByName(String name, SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierRepository.findByName(name).orElseGet(() -> {
                log.info("updateSupplierByName: Supplier with name: {} not found", name);
                throw new ResourceNotFoundException("Supplier with name: " + name + " not found");
            });

            Contact contact = Contact.builder()
                    .phone(supplierRequestDto.getContact().getPhone())
                    .email(supplierRequestDto.getContact().getEmail())
                    .address(supplierRequestDto.getContact().getAddress())
                    .build();
            supplier.setContact(contact);
            supplier.setName(supplierRequestDto.getName());

            supplierRepository.save(supplier);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
        } catch (DataAccessException e) {
            log.info("updateSupplierByName: Something went wrong while updating the product: {}", supplierRequestDto != null ? supplierRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (supplierRequestDto != null ? supplierRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteSupplierById(Long id) {
        try {
            if (!supplierRepository.existsById(id)) {
                log.info("deleteSupplierById: Supplier with id: {} not found", id);
                throw new ResourceNotFoundException("Supplier with id: " + id + " not found");
            }
            log.info("deleteSupplierById: Product deleted successfully!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.info("deleteSupplierById: Something went wrong while deleting the product by id: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteSupplierByName(String name) {
        try {
            Supplier supplier = supplierRepository.findByName(name).orElseGet(() -> {
                log.info("deleteSupplierByName: Supplier with name: {} not found", name);
                throw new ResourceNotFoundException("Product with name: " + name + " not found");
            });
            supplierRepository.delete(supplier);
            log.info("deleteSupplierByName: Product deleted successfully!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.info("deleteSupplierByName: Something went wrong while deleting the product by name: {}", name);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<SupplierResponseDto>> getAllSupplier() {
        try {
            List<Supplier> supplierList = supplierRepository.findAll();
            List<SupplierResponseDto> supplierResponseDtos = supplierList.stream().map(supplierMapper::convertToResponseDto).toList();
            log.info("getAllSupplier: Products list retrieved successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(supplierResponseDtos);
        } catch (DataAccessException e) {
            log.error("getAllSupplier: Something went wrong while retrieving category", e);
            throw new RuntimeException("An error occurred while retrieving category", e.getCause());
        }
    }

    public ResponseEntity<SupplierResponseDto> getSupplierById(Long id) {
        return supplierRepository.findById(id).map(
                supplier -> {
                    log.info("getSupplierById: Supplier found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
                }
        ).orElseGet(() -> {
            log.info("getSupplierById: Supplier with id: {} not found", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        });
    }

    public ResponseEntity<SupplierResponseDto> getSupplierByName(String name) {
        return supplierRepository.findByName(name).map(supplier -> {
            log.info("getSupplierByName: Supplier found with name: {}", name);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
        }).orElseGet(() -> {
            log.info("getSupplierByName: Supplier with name: {} not found", name);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        });
    }
}