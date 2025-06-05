package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseRequestDto;
import com.example.inventoryManagementRetail.dto.PurchaseDto.PurchaseResponseDto;
import com.example.inventoryManagementRetail.dto.SupplierDto.SupplierRequestDto;
import com.example.inventoryManagementRetail.exception.DataPersistException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.mapper.PurchaseMapper;
import com.example.inventoryManagementRetail.mapper.SupplierMapper;
import com.example.inventoryManagementRetail.model.Purchase;
import com.example.inventoryManagementRetail.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            ProductRequestDto productRequestDto = productMapper.convertToRequestDto(purchaseRequestDto.getProduct());
            SupplierRequestDto supplierRequestDto = supplierMapper.convertToRequestDto(purchaseRequestDto.getSupplier());
            Purchase purchase = purchaseMapper.convertDtoToEntity(purchaseRequestDto);
            if (productService.existsByName(purchaseRequestDto.getProduct().getName())) {
                productService.updateProductByName(purchaseRequestDto.getProduct().getName(), productRequestDto);
            } else {
                productService.addProduct(productRequestDto);
            }
            if (!supplierService.existsByName(purchaseRequestDto.getSupplier().getName())) {
                supplierService.addSupplier(supplierRequestDto);
            }
            Purchase purchase1 = purchaseRepository.save(purchase);
            PurchaseResponseDto purchaseResponseDto = purchaseMapper.convertEntityToDto(purchase1);
            log.info("Purchase saved successfully: purchaseId={}", purchase.getId());
            return ResponseEntity.status(HttpStatus.OK).body(purchaseResponseDto);
        } catch (DataAccessException e) {
            log.error("Error saving purchase: error={}", e.getMessage(), e);
            throw new DataPersistException("An error occurred while saving the purchase");
        }
    }
}