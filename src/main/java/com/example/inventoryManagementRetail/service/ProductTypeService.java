package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeRequestDto;
import com.example.inventoryManagementRetail.dto.ProductTypeDto.ProductTypeResponseDto;
import com.example.inventoryManagementRetail.exception.DataPersistException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.ProductTypeMapper;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.repository.ProductTypeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

    @Transactional
    public void deleteProductTypeByName(String name) {
        try {
            ProductType productType = productTypeRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productTypeRepository.delete(productType);
            log.info("Product type deleted by name: name={}, id={}", productType.getName(), productType.getId());
        } catch (DataAccessException e) {
            log.error("Error deleting product type by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the product type by name: " + name);
        }
    }

    @Transactional
    public ProductTypeResponseDto addProductType(ProductTypeRequestDto productTypeRequestDto) {
        try {
            if (productTypeRepository.existsByName(productTypeRequestDto.getName())) {
                log.warn("Attempt to add duplicate product type: name={}", productTypeRequestDto.getName());
                throw new DuplicateResourceException("ProductType with name " + productTypeRequestDto.getName() + " already exists");
            }
            ProductType productType = productTypeMapper.convertDtoToEntity(productTypeRequestDto);
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("ProductType added successfully: id={}, name={}", productTypeSaved.getId(), productTypeSaved.getName());
            return productTypeMapper.convertEntityToDto(productTypeSaved);
        } catch (DuplicateResourceException e) {
            log.error("Duplicate product type detected: name={}", productTypeRequestDto.getName(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error adding product type: name={}, error={}", productTypeRequestDto.getName(), e.getMessage(), e);
            throw new DataPersistException("An error occurred while adding the product type");
        }
    }

    public List<ProductTypeResponseDto> getAllProductType() {
        try {
            List<ProductType> productTypeList = productTypeRepository.findAll();
            if (productTypeList.isEmpty()) {
                log.info("No product types found");
                return Collections.emptyList();
            }
            List<ProductTypeResponseDto> productTypeResponseDtos = productTypeList.stream().map(productTypeMapper::convertEntityToDto).toList();
            log.info("Product types retrieved successfully: count={}", productTypeResponseDtos.size());
            return productTypeResponseDtos;
        } catch (DataAccessException e) {
            log.error("Error retrieving product types: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while fetching all product types");
        }
    }

    public ProductTypeResponseDto getProductTypeById(Long id) {
        return productTypeRepository
                .findById(id)
                .map(productType -> {
                    log.info("Product type retrieved by id: id={}", id);
                    return productTypeMapper.convertEntityToDto(productType);
                })
                .orElseThrow(() -> {
                    log.warn("Product type not found by id: id={}", id);
                    return new ResourceNotFoundException("Product type with id: " + id + " not found");
                });
    }

    @Transactional
    public ProductTypeResponseDto getProductTypeByName(String name) {
        return productTypeRepository.findByName(name).map(productType -> {
            log.info("Product type retrieved by name: name={}", name);
            return productTypeMapper.convertEntityToDto(productType);
        }).orElseThrow(() -> {
            log.warn("Product type not found by name: name={}", name);
            return new ResourceNotFoundException("Product type with name: " + name + " not found");
        });
    }

    @Transactional
    public void deleteProductTypeById(Long id) {
        try {
            ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            productTypeRepository.delete(productType);
            log.info("Product type deleted by id: id={}, name={}", productType.getId(), productType.getName());
        } catch (DataAccessException e) {
            log.error("Error deleting product type by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the product type by id: " + id);
        }
    }

    @Transactional
    public ProductTypeResponseDto updateProductTypeById(Long id, ProductTypeRequestDto productTypeRequestDto) {
        try {
            ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            if (!productType.getName().equals(productTypeRequestDto.getName()) && productTypeRepository.existsByName(productTypeRequestDto.getName())) {
                log.warn("Attempt to update product type to a duplicate name: currentName={}, newName={}", productType.getName(), productTypeRequestDto.getName());
                throw new DuplicateResourceException("Name already exists");
            }
            productType.setName(productTypeRequestDto.getName());
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("Product type updated by id: id={}, name={}", productTypeSaved.getId(), productTypeSaved.getName());
            return productTypeMapper.convertEntityToDto(productTypeSaved);
        } catch (DataAccessException e) {
            log.error("Error updating product type by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the product type by id: " + id);
        }
    }

    @Transactional
    public ProductTypeResponseDto updateProductTypeByName(String name, ProductTypeRequestDto productTypeRequestDto) {
        try {
            ProductType productType = productTypeRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product type not found"));
            if (!productType.getName().equals(productTypeRequestDto.getName()) && productTypeRepository.existsByName(productTypeRequestDto.getName())) {
                log.warn("Attempt to update product type to a duplicate name: currentName={}, newName={}", productType.getName(), productTypeRequestDto.getName());
                throw new DuplicateResourceException("Name already exists");
            }
            productType.setName(productTypeRequestDto.getName());
            ProductType productTypeSaved = productTypeRepository.save(productType);
            log.info("Product type updated by name: currentName={}, newName={}", name, productTypeSaved.getName());
            return productTypeMapper.convertEntityToDto(productTypeSaved);
        } catch (DataAccessException e) {
            log.error("Error updating product type by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the product type by name: " + name);
        }
    }
}