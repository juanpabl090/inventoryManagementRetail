package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.exception.ResourceNotFoundException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ResponseEntity<ProductResponseDto> addProduct(ProductRequestDto productRequestDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Product product = productMapper.convertToEntity(productRequestDto);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.convertToResponseDto(product));
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Optional<List<ProductResponseDto>> productDtos = Optional.of(products.stream().map(productMapper::convertToResponseDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(productDtos.get());
    }

    public ResponseEntity<ProductResponseDto> getProductByid(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .body(productMapper.convertToResponseDto(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .build());
    }

    public ResponseEntity<ProductResponseDto> getProductByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        return product.map(value -> ResponseEntity.status(HttpStatus.FOUND).body(productMapper.convertToResponseDto(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProductsById(Long id) {
        List<ProductResponseDto> productList = productRepository
                .findById(id)
                .map(List::of)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"))
                .stream()
                .map(productMapper::convertToResponseDto)
                .toList();
        return ResponseEntity.status(HttpStatus.FOUND).body(productList);
    }

    public ResponseEntity<List<ProductResponseDto>> getAllProductsByName(String name) {
        List<ProductResponseDto> productList = productRepository
                .findByName(name)
                .map(List::of)
                .orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"))
                .stream()
                .map(productMapper::convertToResponseDto)
                .toList();
        return ResponseEntity.status(HttpStatus.FOUND).body(productList);
    }

    @Transactional
    public ResponseEntity<?> deleteProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
        productRepository.delete(product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Transactional
    public ResponseEntity<?> deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}