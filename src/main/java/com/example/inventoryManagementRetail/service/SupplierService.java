package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
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
            if (supplierRepository.existsByName(supplierRequestDto.getName())) {
                log.error("Supplier with name is already exist: {}", supplierRequestDto.getName());
                throw new DuplicateResourceException("Supplier with name: " + supplierRequestDto.getName() + " already exists");
            }
            Supplier supplier = supplierMapper.convertToEntity(supplierRequestDto);
            Contact contact = new Contact();
            contact.setAddress(supplierRequestDto.getContact().getAddress());
            contact.setEmail(supplierRequestDto.getContact().getEmail());
            contact.setPhone(supplierRequestDto.getContact().getPhone());
            supplier.setContact(contact);
            supplierRepository.save(supplier);
            log.info("Supplier and its contact added successfully: {}", supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(supplierMapper.convertToResponseDto(supplier));
        } catch (DuplicateResourceException e) {
            log.error("Something went wrong while checking if supplier exists by name: {}", supplierRequestDto.getName());
            throw e;
        } catch (DataAccessException e) {
            log.info("Something went wrong while saving the product", e);
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
            Supplier supplierSaved = supplierRepository.save(supplier);
            log.info("Supplier updated By id successfully: {}", supplierSaved);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DataAccessException e) {
            log.info("Something went wrong while updating By id the product: {}", supplierRequestDto != null ? supplierRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (supplierRequestDto != null ? supplierRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updateSupplierByName(String name, SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierRepository.findByName(name).orElseGet(() -> {
                log.info("Supplier with name: {} not found", name);
                throw new ResourceNotFoundException("Supplier with name: " + name + " not found");
            });
            Contact contact = Contact.builder()
                    .phone(supplierRequestDto.getContact().getPhone())
                    .email(supplierRequestDto.getContact().getEmail())
                    .address(supplierRequestDto.getContact().getAddress())
                    .build();
            supplier.setContact(contact);
            supplier.setName(supplierRequestDto.getName());
            Supplier supplierSaved = supplierRepository.save(supplier);
            log.info("Supplier updated By name successfully: {}", supplierSaved);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DataAccessException e) {
            log.info("Something went wrong while updating by name the product: {}", supplierRequestDto != null ? supplierRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (supplierRequestDto != null ? supplierRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteSupplierById(Long id) {
        try {
            if (!supplierRepository.existsById(id)) {
                log.info("Supplier with id: {} not found", id);
                throw new ResourceNotFoundException("Supplier with id: " + id + " not found");
            }
            supplierRepository.deleteById(id);
            log.info("Product deleted by id successfully: {}", id);
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
                log.info("Supplier with name: {} not found", name);
                throw new ResourceNotFoundException("Product with name: " + name + " not found");
            });
            supplierRepository.delete(supplier);
            log.info("Product deleted by name successfully: {}", name);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.info("Something went wrong while deleting by name the product: {}", name);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<SupplierResponseDto>> getAllSupplier() {
        try {
            List<Supplier> supplierList = supplierRepository.findAll();
            List<SupplierResponseDto> supplierResponseDtos = supplierList.stream().map(supplierMapper::convertToResponseDto).toList();
            log.info("Products list retrieved successfully");
            return ResponseEntity.status(HttpStatus.OK).body(supplierResponseDtos);
        } catch (DataAccessException e) {
            log.error("Something went wrong while retrieving category", e);
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