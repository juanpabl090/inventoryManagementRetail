package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.exception.BusinessValidationException;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.model.Category;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.model.ProductType;
import com.example.inventoryManagementRetail.model.Supplier;
import com.example.inventoryManagementRetail.repository.CategoryRepository;
import com.example.inventoryManagementRetail.repository.ProductRepository;
import com.example.inventoryManagementRetail.repository.ProductTypeRepository;
import com.example.inventoryManagementRetail.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository, ProductTypeRepository productTypeRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.productTypeRepository = productTypeRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto productRequestDto) {
        try {
            if (productRepository.existsByName(productRequestDto.getName())) {
                log.warn("Attempt to add duplicate product: name={}", productRequestDto.getName());
                throw new DuplicateResourceException("Product with name: " + productRequestDto.getName() + " already exists");
            }
            Product product = productMapper.convertToEntity(productRequestDto);
            product.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            product.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            Product productSaved = productRepository.save(product);
            log.info("Product added successfully: id={}, name={}", productSaved.getId(), productSaved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.convertToResponseDto(productSaved));
        } catch (DuplicateResourceException e) {
            log.error("Duplicate product detected: name={}", productRequestDto.getName(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error saving product: name={}, error={}", productRequestDto.getName(), e.getMessage(), e);
            throw new RuntimeException("An error occurred while saving the product", e.getCause());
        }
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                log.info("No products found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            List<ProductResponseDto> productDtos = products.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved successfully: count={}", productDtos.size());
            return ResponseEntity.status(HttpStatus.OK).body(productDtos);
        } catch (DataAccessException e) {
            log.error("Error retrieving products: error={}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while getting the products", e);
        }
    }

    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(value -> {
                    log.info("Product retrieved by id: id={}", id);
                    return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(value));
                })
                .orElseThrow(() -> {
                    log.warn("Product not found by id: id={}", id);
                    return new ResourceNotFoundException("Product with id: " + id + " not found");
                });
    }

    public ResponseEntity<ProductResponseDto> getProductByName(String name) {
        return productRepository.findByName(name)
                .map(product -> {
                    log.info("Product retrieved by name: name={}", name);
                    return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(product));
                })
                .orElseThrow(() -> {
                    log.warn("Product not found by name: name={}", name);
                    return new ResourceNotFoundException("Product with name: " + name + " not found");
                });
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategory(Long categoryId) {
        try {
            List<Product> productList = productRepository.getAllProductsByCategory(categoryId);
            if (productList.isEmpty()) {
                log.info("No products found for category: id={}", categoryId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            List<ProductResponseDto> productListDto = productList.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved by category: id={}, count={}", categoryId, productListDto.size());
            return ResponseEntity.status(HttpStatus.OK).body(productListDto);
        } catch (DataAccessException e) {
            log.error("Error retrieving products by category: id={}, error={}", categoryId, e.getMessage(), e);
            throw new RuntimeException("An error occurred while getting the products by category: " + categoryId, e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> updateProductById(Long id, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            Product productUpdated = updateProduct(product, productRequestDto);
            Product productSaved = productRepository.save(productUpdated);
            log.info("Product updated by id: id={}, name={}", productSaved.getId(), productSaved.getName());
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(productSaved));
        } catch (DataAccessException e) {
            log.error("Error updating product by id: id={}, error={}", id, e.getMessage(), e);
            throw new RuntimeException("An error occurred while updating the product: " + id, e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> updateProductByName(String name, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            Product productUpdated = updateProduct(product, productRequestDto);
            Product productSaved = productRepository.save(productUpdated);
            log.info("Product updated by name: name={}, id={}", productSaved.getName(), productSaved.getId());
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(productSaved));
        } catch (DataAccessException e) {
            log.error("Error updating product by name: name={}, error={}", name, e.getMessage(), e);
            throw new RuntimeException("An error occurred while updating the product: " + name, e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteProductByName(String name) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            productRepository.delete(product);
            log.info("Product deleted by name: name={}, id={}", product.getName(), product.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.error("Error deleting product by name: name={}, error={}", name, e.getMessage(), e);
            throw new RuntimeException("An error occurred while deleting the product: " + name, e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteProductById(Long id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            productRepository.delete(product);
            log.info("Product deleted by id: id={}, name={}", product.getId(), product.getName());
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Error deleting product by id: id={}, error={}", id, e.getMessage(), e);
            throw new RuntimeException("An error occurred while deleting the product: " + id, e.getCause());
        }
    }

    private Product updateProduct(Product product, ProductRequestDto productRequestDto) {
        Category category = categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category with id: " + productRequestDto.getCategoryId() + " not found"));
        Supplier supplier = supplierRepository.findById(productRequestDto.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier with id: " + productRequestDto.getSupplierId() + " not found"));
        ProductType productType = productTypeRepository.findById(productRequestDto.getProductTypeId()).orElseThrow(() -> new ResourceNotFoundException("Product type with id: " + productRequestDto.getProductTypeId() + " not found"));
        if (productRequestDto.getSalePrice().compareTo(productRequestDto.getBuyPrice()) < 0) {
            throw new BusinessValidationException("Sale price must be >= buy price");
        }
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setCategory(category);
        product.setBuyPrice(productRequestDto.getBuyPrice());
        product.setSalePrice(productRequestDto.getSalePrice());
        product.setStock(productRequestDto.getStock());
        product.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        product.setSupplier(supplier);
        product.setProductType(productType);
        return product;
    }
}