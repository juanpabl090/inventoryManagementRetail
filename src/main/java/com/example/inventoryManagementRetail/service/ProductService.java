package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseWithDetailsDto;
import com.example.inventoryManagementRetail.exception.BusinessValidationException;
import com.example.inventoryManagementRetail.exception.DataPersistException;
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
    public ResponseEntity<ProductResponseDto> findOrCreateProduct(ProductRequestDto productRequestDto) {
        return productRepository.findByName(productRequestDto.getName())
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(productMapper.convertToResponseDto(product)))
                .orElseGet(() -> {
                    Product product = productMapper.convertToEntity(productRequestDto);
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                    product.setCreatedDate(now);
                    product.setUpdatedDate(now);
                    Product savedProduct = productRepository.save(product);
                    return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.convertToResponseDto(savedProduct));
                });
    }

    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
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
            return productMapper.convertToResponseDto(productSaved);
        } catch (DuplicateResourceException e) {
            log.error("Duplicate product detected: name={}", productRequestDto.getName(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error saving product: name={}, error={}", productRequestDto.getName(), e.getMessage(), e);
            throw new DataPersistException("An error occurred while saving the product");
        }
    }

    public List<ProductResponseDto> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                log.info("No products found");
                return Collections.emptyList();
            }
            List<ProductResponseDto> productDtos = products.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved successfully: count={}", productDtos.size());
            return productDtos;
        } catch (DataAccessException e) {
            log.error("Error retrieving products: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while getting the products");
        }
    }

    public List<ProductResponseWithDetailsDto> getAllProductsWithDetails() {
        try {
            List<Product> productsResponse = productRepository.findWithDetails();
            if (productsResponse.isEmpty()) {
                log.info("No Products Found");
                return Collections.emptyList();
            }
            log.info("Products retrieved successfully: count={}", productsResponse.size());
            return productsResponse.stream().map(productMapper::convertEntityToProductResponseWithDetailsDto).toList();
        } catch (DataAccessException e) {
            log.error("Error retrieving products: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while getting the products");
        }
    }

    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(value -> {
                    log.info("Product retrieved by id: id={}", id);
                    return productMapper.convertToResponseDto(value);
                })
                .orElseThrow(() -> {
                    log.warn("Product not found by id: id={}", id);
                    return new ResourceNotFoundException("Product with id: " + id + " not found");
                });
    }

    public ProductResponseDto getProductByName(String name) {
        return productRepository.findByName(name)
                .map(product -> {
                    log.info("Product retrieved by name: name={}", name);
                    return productMapper.convertToResponseDto(product);
                })
                .orElseThrow(() -> {
                    log.warn("Product not found by name: name={}", name);
                    return new ResourceNotFoundException("Product with name: " + name + " not found");
                });
    }

    public Product getProductEntityByName(String name) {
        return productRepository.findByName(name).orElseThrow(() -> {
            log.warn("product not found: {}", name);
            return new ResourceNotFoundException("product not found by name: " + name + ".");
        });
    }

    public List<ProductResponseDto> getAllProductsByCategory(Long categoryId) {
        try {
            List<Product> productList = productRepository.getAllProductsByCategory(categoryId);
            if (productList.isEmpty()) {
                log.info("No products found for category: id={}", categoryId);
                return Collections.emptyList();
            }
            List<ProductResponseDto> productListDto = productList.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved by category: id={}, count={}", categoryId, productListDto.size());
            return productListDto;
        } catch (DataAccessException e) {
            log.error("Error retrieving products by category: id={}, error={}", categoryId, e.getMessage(), e);
            throw new DataPersistException("An error occurred while getting the products by category: " + categoryId);
        }
    }

    public List<ProductResponseDto> getProductsByProductType(String productTypeName) {
        try {
            List<Product> productList = productRepository.getAllProductsByProductType(productTypeName).orElseThrow(() -> new ResourceNotFoundException("Product type with name: " + productTypeName + " not found"));
            if (productList.isEmpty()) {
                log.info("No products found for product type: name={}", productTypeName);
                return Collections.emptyList();
            }
            List<ProductResponseDto> productListResponse = productList.stream().map(productMapper::convertToResponseDto).toList();
            log.info("Products retrieved by product type: name={}, count={}", productTypeName, productListResponse.size());
            return productListResponse;
        } catch (ResourceNotFoundException e) {
            log.warn("Product type not found by name: name={}", productTypeName, e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Error retrieving products by product type: name={}, error={}", productTypeName, e.getMessage(), e);
            throw new DataPersistException("An error occurred while getting the products by product type: " + productTypeName);
        }
    }

    @Transactional
    public ProductResponseDto updateProductById(Long id, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            Product productUpdated = updateProduct(product, productRequestDto);
            Product productSaved = productRepository.save(productUpdated);
            log.info("Product updated by id: id={}, name={}", productSaved.getId(), productSaved.getName());
            return productMapper.convertToResponseDto(productSaved);
        } catch (DataAccessException e) {
            log.error("Error updating product by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the product: " + id);
        }
    }

    @Transactional
    public ProductResponseDto updateProductByName(String name, ProductRequestDto productRequestDto) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            Product productUpdated = updateProduct(product, productRequestDto);
            Product productSaved = productRepository.save(productUpdated);
            log.info("Product updated by name: name={}, id={}", productSaved.getName(), productSaved.getId());
            return productMapper.convertToResponseDto(productSaved);
        } catch (DataAccessException e) {
            log.error("Error updating product by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while updating the product: " + name);
        }
    }

    @Transactional
    public ProductResponseDto updatePatchProductByName(String name, ProductPatchRequestDto productPatchRequestDto) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            Product productUpdate = updatePatch(productPatchRequestDto, product);
            Product productSaved = productRepository.save(productUpdate);
            log.info("Product patched by name: name={}, id={}", productSaved.getName(), productSaved.getId());
            return productMapper.convertToResponseDto(productSaved);
        } catch (DataAccessException e) {
            log.error("Error patching product by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while patching the product: " + name);
        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for patching by name: name={}", name, e);
            throw e;
        } catch (BusinessValidationException e) {
            log.warn("Business validation failed for patching product by name: name={}, error={}", name, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteProductByName(String name) {
        try {
            Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product with name: " + name + " not found"));
            productRepository.delete(product);
            log.info("Product deleted by name: name={}, id={}", product.getName(), product.getId());
        } catch (DataAccessException e) {
            log.error("Error deleting product by name: name={}, error={}", name, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the product: " + name);
        }
    }

    @Transactional
    public void deleteProductById(Long id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
            productRepository.delete(product);
            log.info("Product deleted by id: id={}, name={}", product.getId(), product.getName());
        } catch (DataAccessException e) {
            log.error("Error deleting product by id: id={}, error={}", id, e.getMessage(), e);
            throw new DataPersistException("An error occurred while deleting the product: " + id);
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

    private Product updatePatch(ProductPatchRequestDto productPatchRequestDto, Product product) {
        if (productPatchRequestDto.getName() != null) product.setName(productPatchRequestDto.getName());
        if (productPatchRequestDto.getDescription() != null)
            product.setDescription(productPatchRequestDto.getDescription());
        if (productPatchRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productPatchRequestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + productPatchRequestDto.getCategoryId() + " not found"));
            product.setCategory(category);
        }
        if (productPatchRequestDto.getBuyPrice() != null) product.setBuyPrice(productPatchRequestDto.getBuyPrice());
        if (productPatchRequestDto.getSalePrice() != null)
            product.setSalePrice(productPatchRequestDto.getSalePrice());
        if (productPatchRequestDto.getStock() != null) product.setStock(productPatchRequestDto.getStock());
        product.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        if (productPatchRequestDto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productPatchRequestDto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier with id: " + productPatchRequestDto.getSupplierId() + " not found"));
            product.setSupplier(supplier);
        }
        if (productPatchRequestDto.getProductTypeId() != null) {
            ProductType productType = productTypeRepository.findById(productPatchRequestDto.getProductTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product type with id: " + productPatchRequestDto.getProductTypeId() + " not found"));
            product.setProductType(productType);
        }
        return product;
    }

    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}