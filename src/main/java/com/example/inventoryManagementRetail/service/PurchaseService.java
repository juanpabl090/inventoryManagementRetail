package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseResponseDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierPatchRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierResponseDto;
import com.example.inventoryManagementRetail.exception.DataPersistException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.mapper.PurchaseMapper;
import com.example.inventoryManagementRetail.mapper.SupplierMapper;
import com.example.inventoryManagementRetail.model.Product;
import com.example.inventoryManagementRetail.model.Purchase;
import com.example.inventoryManagementRetail.model.Supplier;
import com.example.inventoryManagementRetail.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final ProductMapper productMapper;
    private final SupplierMapper supplierMapper;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductService productService, SupplierService supplierService, ProductMapper productMapper, SupplierMapper supplierMapper, PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.supplierService = supplierService;
        this.productMapper = productMapper;
        this.supplierMapper = supplierMapper;
        this.purchaseMapper = purchaseMapper;
    }

    @Transactional
    public ResponseEntity<PurchaseResponseDto> registerPurchase(PurchaseRequestDto purchaseRequestDto) {
        try {
            ProductRequestDto productRequestDto = purchaseRequestDto.getProduct();
            SupplierRequestDto supplierRequestDto = purchaseRequestDto.getSupplier();

            // actualizar o crear Supplier
            ResponseEntity<SupplierResponseDto> supplierResponseDto = supplierService.findOrCreateSupplier(supplierRequestDto);
            Supplier supplier = supplierMapper.convertResponseDtoToEntity(supplierResponseDto.getBody());
            SupplierPatchRequestDto supplierPatchRequestDto = supplierMapper.convertResponseDtoToPatchDto(supplierResponseDto.getBody());
            if (supplierResponseDto.getStatusCode() == HttpStatus.OK) {
                supplierService.updatePatchSupplierByName(supplierResponseDto.getBody().getName(), supplierPatchRequestDto);
            }

            //asignar el id del proveedor al producto
            productRequestDto.setSupplierId(supplierResponseDto.getBody().getId());

            // actualizar o crear producto
            ResponseEntity<ProductResponseDto> productResponseDto = productService.findOrCreateProduct(productRequestDto);
            Product product = productMapper.convertResponseDtoToEntity(productResponseDto.getBody());
            ProductPatchRequestDto productPatchRequestDto = productMapper.convertResponseDtoToPatchDto(productResponseDto.getBody());
            if (productResponseDto.getStatusCode() == HttpStatus.OK) {
                Long nuevoStock = product.getStock() + purchaseRequestDto.getQuantity();
                productPatchRequestDto.setStock(nuevoStock);
                productService.updatePatchProductByName(productResponseDto.getBody().getName(), productPatchRequestDto);
            }

            // Register purchase
            Purchase purchase = purchaseMapper.convertDtoToEntity(purchaseRequestDto);
            purchase.setProduct(product);
            purchase.setSupplier(supplier);
            purchase.setDate(LocalDateTime.now(ZoneId.of("UTC")));
            Purchase savedPurchase = purchaseRepository.save(purchase);
            PurchaseResponseDto purchaseResponseDto = purchaseMapper.convertEntityToDto(savedPurchase);

            log.info("Purchase saved successfully: purchaseId={}", purchase.getId());
            return ResponseEntity.status(HttpStatus.OK).body(purchaseResponseDto);
        } catch (DataAccessException e) {
            log.error("Error saving purchase: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while saving the purchase");
        }
    }
}