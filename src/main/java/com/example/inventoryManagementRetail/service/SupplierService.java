package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierPatchRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.exception.BusinessValidationException;
import com.example.inventoryManagementRetail.exception.DataPersistException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.SupplierMapper;
import com.example.inventoryManagementRetail.model.Contact;
import com.example.inventoryManagementRetail.model.Supplier;
import com.example.inventoryManagementRetail.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public ResponseEntity<SupplierResponseDto> findOrCreateSupplier(SupplierRequestDto supplierRequestDto) {
        return supplierRepository.findByName(supplierRequestDto.getName())
                .map(supplier -> ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier)))
                .orElseGet(() -> {
                    Supplier supplier = supplierMapper.convertToEntity(supplierRequestDto);
                    Supplier savedSupplier = supplierRepository.save(supplier);
                    return ResponseEntity.status(HttpStatus.CREATED).body(supplierMapper.convertToResponseDto(savedSupplier));
                });
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> addSupplier(SupplierRequestDto supplierRequestDto) {
        try {
            if (supplierRepository.existsByName(supplierRequestDto.getName())) {
                log.warn("Attempt to add duplicate supplier: name={}", supplierRequestDto.getName());
                throw new DuplicateResourceException("Supplier with name: " + supplierRequestDto.getName() + " already exists");
            }
            Supplier supplier = supplierMapper.convertToEntity(supplierRequestDto);
            Supplier supplierSaved = supplierRepository.save(supplier);
            log.info("Supplier added successfully: id={}, name={}", supplierSaved.getId(), supplierSaved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DuplicateResourceException e) {
            log.error("Duplicate supplier detected: name={}", supplierRequestDto.getName(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error saving supplier: name={}, error={}", supplierRequestDto.getName(), e.getMessage(), e);
            throw new DataPersistException("An error occurred while saving the supplier");
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updateSupplierById(Long id, SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> {
                log.warn("Supplier not found by id: id={}", id);
                return new ResourceNotFoundException("Supplier with id: " + id + " not found");
            });
            if (!supplierRequestDto.getName().equals(supplier.getName()) && supplierRepository.existsByName(supplierRequestDto.getName())) {
                log.warn("Attempt to update supplier to a duplicate name: currentName={}, newName={}", supplier.getName(), supplierRequestDto.getName());
                throw new DuplicateResourceException("Supplier with name: " + supplierRequestDto.getName() + " already exists");
            }
            Supplier supplierUpdated = updateSupplier(supplier, supplierRequestDto);
            Supplier supplierSaved = supplierRepository.save(supplierUpdated);
            log.info("Supplier updated successfully by id: id={}, name={}", supplierSaved.getId(), supplierSaved.getName());
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DataAccessException e) {
            log.error("Error updating supplier by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the supplier: " + id);
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updateSupplierByName(String name, SupplierRequestDto supplierRequestDto) {
        try {
            Supplier supplier = supplierRepository.findByName(name).orElseThrow(() -> {
                log.warn("Supplier not found by name: name={}", name);
                return new ResourceNotFoundException("Supplier with name: " + name + " not found");
            });
            if (!supplierRequestDto.getName().equals(supplier.getName()) && supplierRepository.existsByName(supplierRequestDto.getName())) {
                log.warn("Attempt to update supplier to a duplicate name: currentName={}, newName={}", supplier.getName(), supplierRequestDto.getName());
                throw new DuplicateResourceException("Supplier with name: " + supplierRequestDto.getName() + " already exists");
            }
            Supplier supplierUpdated = updateSupplier(supplier, supplierRequestDto);
            Supplier supplierSaved = supplierRepository.save(supplierUpdated);
            log.info("Supplier updated successfully by name: currentName={}, newName={}", name, supplierSaved.getName());
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DataAccessException e) {
            log.error("Error updating supplier by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the supplier: " + name);
        }
    }

    @Transactional
    public ResponseEntity<SupplierResponseDto> updatePatchSupplierByName(String name,SupplierPatchRequestDto supplierPatchRequestDto) {
        try {
            Supplier supplier = supplierRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Supplier with name: " + name + " not found"));
            Supplier supplierUpdated = updatePatchSupplier(supplier, supplierPatchRequestDto);
            Supplier supplierSaved = supplierRepository.save(supplierUpdated);
            log.info("Supplier patched successfully by name: currentName={}, newName={}", name, supplierSaved.getName());
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplierSaved));
        } catch (DataAccessException e) {
            log.error("Error patching supplier by name: name={}, error={}", supplierPatchRequestDto.getName(), e.getMessage(), e);
            throw new DataPersistException("An error occurred while patching the supplier: " + supplierPatchRequestDto.getName());
        } catch (ResourceNotFoundException e) {
            log.warn("Supplier not found for patching by name: name={}", supplierPatchRequestDto.getName());
            throw e;
        } catch (BusinessValidationException e) {
            log.warn("Supplier patching validation failed: name={}, error={}", supplierPatchRequestDto.getName(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteSupplierById(Long id) {
        try {
            supplierRepository.deleteById(id);
            log.info("Supplier deleted successfully by id: id={}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Supplier not found for deletion by id: id={}", id);
            throw new ResourceNotFoundException("Supplier with id: " + id + " not found");
        } catch (DataAccessException e) {
            log.error("Error deleting supplier by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the supplier by id: " + id);
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteSupplierByName(String name) {
        try {
            supplierRepository.deleteByName(name);
            log.info("Supplier deleted successfully by name: name={}", name);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Supplier not found for deletion by name: name={}", name);
            throw new ResourceNotFoundException("Supplier with name: " + name + " not found");
        } catch (DataAccessException e) {
            log.error("Error deleting supplier by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the supplier by name: " + name);
        }
    }

    public ResponseEntity<List<SupplierResponseDto>> getAllSupplier() {
        try {
            List<Supplier> supplierList = supplierRepository.findAll();
            if (supplierList.isEmpty()) {
                log.info("No suppliers found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            List<SupplierResponseDto> supplierResponseDtos = supplierList.stream().map(supplierMapper::convertToResponseDto).toList();
            log.info("Suppliers retrieved successfully: count={}", supplierResponseDtos.size());
            return ResponseEntity.status(HttpStatus.OK).body(supplierResponseDtos);
        } catch (DataAccessException e) {
            log.error("Error retrieving suppliers: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while retrieving suppliers");
        }
    }

    public ResponseEntity<SupplierResponseDto> getSupplierById(Long id) {
        return supplierRepository.findById(id).map(
                supplier -> {
                    log.info("Supplier retrieved by id: id={}", id);
                    return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
                }
        ).orElseThrow(() -> {
            log.warn("Supplier not found by id: id={}", id);
            return new ResourceNotFoundException("Supplier with id: " + id + " not found");
        });
    }

    public ResponseEntity<SupplierResponseDto> getSupplierByName(String name) {
        return supplierRepository.findByName(name).map(supplier -> {
            log.info("Supplier retrieved by name: name={}", name);
            return ResponseEntity.status(HttpStatus.OK).body(supplierMapper.convertToResponseDto(supplier));
        }).orElseThrow(() -> {
            log.warn("Supplier not found by name: name={}", name);
            return new ResourceNotFoundException("Supplier with name: " + name + " not found");
        });
    }

    private Supplier updateSupplier(Supplier supplier, SupplierRequestDto supplierRequestDto) {
        supplier.setName(supplierRequestDto.getName());
        Contact contact = supplier.getContact();
        if (contact != null) {
            Contact contactData = supplierRequestDto.getContact();
            contact.setEmail(contactData.getEmail());
            contact.setPhone(contactData.getPhone());
            contact.setAddress(contactData.getAddress());
        } else {
            supplier.setContact(supplierRequestDto.getContact());
        }
        return supplier;
    }

    private Supplier updatePatchSupplier(Supplier supplier, SupplierPatchRequestDto supplierPatchRequestDto) {
        if (supplierPatchRequestDto.getName() != null) supplier.setName(supplierPatchRequestDto.getName());
        Contact contact = supplier.getContact();
        if (supplierPatchRequestDto.getContact() != null) {
            Contact contactData = supplierPatchRequestDto.getContact();
            contact.setPhone(contactData.getPhone());
            contact.setEmail(contactData.getEmail());
            contact.setAddress(contactData.getAddress());
        }
        supplier.setContact(contact);
        return supplier;
    }

    public boolean existsByName(String name) {
        return supplierRepository.existsByName(name);
    }
}