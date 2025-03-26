package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.exception.DuplicateResourceException;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.repository.ProductRepository;
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

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto productRequestDto) {
        try {
            if (productRepository.existsByName(productRequestDto.getName())) {
                throw new DuplicateResourceException("Product with name: " + productRequestDto.getName() + " already exists");
            }
            Product product = productMapper.convertToEntity(productRequestDto);
            product.setCreated_date(LocalDateTime.now(ZoneId.of("UTC")));
            product.setUpdated_date(LocalDateTime.now(ZoneId.of("UTC")));
            productRepository.save(product);
            log.info("Product added successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.convertToResponseDto(product));
        } catch (DuplicateResourceException e) {
            log.error("Something went wrong while checking if product exists by name: {}", productRequestDto.getName());
            throw e;
        } catch (DataAccessException e) {
            log.error("Something went wrong while saving the product", e);
            throw new RuntimeException("An error occurred while saving the product", e.getCause());
        }
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                log.info("Empty list of products");
                return ResponseEntity.ok(Collections.emptyList());
            }
            List<ProductResponseDto> productDtos = products.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(productDtos);
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting the products", e.getCause());
            throw new RuntimeException("An error occurred while getting the products", e.getCause());
        }
    }

    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(value -> {
                    log.info("Product found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(value));
                })
                .orElseGet(() -> {
                    log.info("Product with id: {} not found", id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                });
    }

    public ResponseEntity<ProductResponseDto> getProductByName(String name) {
        return productRepository.findByName(name)
                .map(product -> {
                    log.info("product found with name: {}", name);
                    return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(product));
                })
                .orElseGet(() -> {
                    log.info("Product with name: {} not found", name);
                    throw new RuntimeException("Product with name: " + name + " not found");
                });
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategory(Long categoryId) {
        try {
            List<Product> productList = productRepository.getAllProductsByCategory(categoryId);
            if (!productList.isEmpty()) {
                List<ProductResponseDto> productListDto = productList.stream().map(productMapper::convertToResponseDto).toList();
                log.info("Products list retrieved successfully by category: {}", categoryId);
                return ResponseEntity.status(HttpStatus.OK).body(productListDto);
            }
            log.info("Products list retrieved successfully by category: {}", categoryId);
            return ResponseEntity.ok(Collections.emptyList());
        } catch (DataAccessException e) {
            log.error("Something went wrong while getting the products by category, {}", categoryId);
            throw new RuntimeException("Something went wrong while getting the products by category", e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> updateProductById(Long id, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            product.setName(productRequestDto.getName());
            product.setDescription(productRequestDto.getDescription());
            product.setCategory(productRequestDto.getCategory());
            product.setBuy_price(productRequestDto.getBuy_price());
            product.setSale_price(productRequestDto.getSale_price());
            product.setStock(productRequestDto.getStock());
            product.setUpdated_date(LocalDateTime.now());
            Product productSaved = productRepository.save(product);
            log.info("Product updated successfully: {}", productSaved);
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(productSaved));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating the product: {}", productRequestDto != null ? productRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (productRequestDto != null ? productRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> updateProductByName(String name, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            product.setName(productRequestDto.getName());
            product.setDescription(productRequestDto.getDescription());
            product.setCategory(productRequestDto.getCategory());
            product.setBuy_price(productRequestDto.getBuy_price());
            product.setSale_price(productRequestDto.getSale_price());
            product.setStock(productRequestDto.getStock());
            product.setUpdated_date(LocalDateTime.now());
            log.info("Product updated successfully: {}", product);
            Product productSaved = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(productSaved));
        } catch (DataAccessException e) {
            log.error("Something went wrong while updating the product: {}", productRequestDto != null ? productRequestDto.getName() : "unknown", e);
            throw new RuntimeException("An error occurred while updating the product: " + (productRequestDto != null ? productRequestDto.getName() : "unknown"), e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProductByName(String name) {
        try {
            Product product = productRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            productRepository.delete(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting the product by name: {}", name);
            throw new RuntimeException("An error occurred while deleting the product: " + name, e.getCause());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProductById(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            log.info("Product deleted successfully: {}", id);
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            log.error("Something went wrong while deleting the product by id: {}", id);
            throw new RuntimeException("An error occurred while deleting the product: " + id, e.getCause());
        }
    }
}