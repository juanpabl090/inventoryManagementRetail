package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeRequestDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.ProductTypeMapper;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.repository.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
    }

    public ResponseEntity<ProductTypeResponseDto> addProductType(ProductTypeRequestDto productTypeRequestDto) {
        try {
            if (productTypeRepository.existsByName(productTypeRequestDto.getName())) {
                log.error("ProductType with name is already exist: {}", productTypeRequestDto.getName());
                throw new DuplicateResourceException("ProductType with name" + productTypeRequestDto.getName() + " already exist");
            }
            ProductType productType = productTypeMapper.convertDtoToEntity(productTypeRequestDto);
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("ProductType added successfully: {}", productTypeSaved);
            return ResponseEntity.status(HttpStatus.CREATED).body(productTypeMapper.convertEntityToDto(productTypeSaved));
        } catch (DuplicateResourceException e) {
            log.error("Something went wrong while checking if product type exists by name: {}", productTypeRequestDto.getName());
            throw e;
        } catch (DataAccessException e) {
            log.error("Something went wrong while adding the product", e);
            throw new RuntimeException("An error occurred while adding the product", e.getCause());
        }
    }

    public ResponseEntity<List<ProductTypeResponseDto>> getAllProductType() {
        try {
            List<ProductType> productTypeList = productTypeRepository.findAll();
            if (productTypeList.isEmpty()) {
                log.info("Empty list of product types");
                return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
            }
            List<ProductTypeResponseDto> productTypeResponseDtos = productTypeList.stream().map(productTypeMapper::convertEntityToDto).toList();
            log.info("Product types retrieved successfully: {}", productTypeResponseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(productTypeResponseDtos);
        } catch (DataAccessException e) {
            log.error("Something went wrong while fetching all product types", e);
            throw new RuntimeException("An error occurred while fetching all product types", e);
        }
    }

    public ResponseEntity<ProductTypeResponseDto> getProductTypeById(Long id) {
        return productTypeRepository
                .findById(id)
                .map(productType -> {
                    log.info("Product type found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.convertEntityToDto(productType));
                })
                .orElseGet(() -> {
                    log.error("Product type not found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                });
    }

    public ResponseEntity<ProductTypeResponseDto> getProductTypeByName(String name) {
        return productTypeRepository.findByName(name).map(productType -> {
            log.info("Product type found with name: {}", name);
            return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.convertEntityToDto(productType));
        }).orElseGet(() -> {
            log.error("Product type not found with name: {}", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        });
    }

    public ResponseEntity<?> deleteProductTypeById(Long id) {
        try {
            ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productTypeRepository.delete(productType);
            log.info("Product type deleted by id successfully: {}", productType);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting the product type by id: {}", id, e);
            throw new RuntimeException("An error occurred while deleting the product type " + id, e);
        }
    }

    public ResponseEntity<?> deleteProductTypeByName(String name) {
        try {
            ProductType productType = productTypeRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productTypeRepository.delete(productType);
            log.info("Product type deleted by name successfully: {}", productType);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting the product type by name: {}", name, e);
            throw new RuntimeException("An error occurred while deleting the product type " + name, e);
        }

    }

    public ResponseEntity<ProductTypeResponseDto> updateProductTypeById(Long id, ProductTypeRequestDto productTypeRequestDto) {
        try {
            ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productType.setName(productTypeRequestDto.getName());
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("Product type updated successfully: {}", productType);
            return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.convertEntityToDto(productTypeSaved));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating the product type by id: {}", productTypeRequestDto != null ? productTypeRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product type " + id, e.getCause());
        }
    }

    public ResponseEntity<ProductTypeResponseDto> updateProductTypeByName(String name, ProductTypeRequestDto productTypeRequestDto) {
        try {
            ProductType productType = productTypeRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productType.setName(productTypeRequestDto.getName());
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("Product type updated by name successfully: {}", productTypeSaved);
            return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.convertEntityToDto(productTypeSaved));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating the product type by name: {}", productTypeRequestDto != null ? productTypeRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product type " + name, e.getCause());
        }
    }
}