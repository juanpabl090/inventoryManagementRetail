package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsRequestDto;
import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsResponseDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.exception.InsufficientResourcesException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.mapper.SaleDetailsMapper;
import com.example.inventoryManagementRetail.mapper.SaleMapper;
import com.example.inventoryManagementRetail.model.Sale;
import com.example.inventoryManagementRetail.model.SaleDetails;
import com.example.inventoryManagementRetail.repository.SaleDetailsRepository;
import com.example.inventoryManagementRetail.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailsRepository saleDetailsRepository;
    private final SaleMapper saleMapper;
    private final SaleDetailsMapper saleDetailsMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;


    public SaleService(SaleRepository saleRepository, SaleDetailsRepository saleDetailsRepository, SaleMapper saleMapper, SaleDetailsMapper saleDetailsMapper, ProductService productService, ProductMapper productMapper) {
        this.saleRepository = saleRepository;
        this.saleDetailsRepository = saleDetailsRepository;
        this.saleMapper = saleMapper;
        this.saleDetailsMapper = saleDetailsMapper;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Transactional
    public SaleResponseDto registerSale(SaleDetailsRequestDto saleDetailsRequestDto) {
        BigDecimal grandTotal = BigDecimal.ZERO;
        // Cache para evitar consultas duplicadas
        Map<String, ProductResponseDto> productsCache = new HashMap<>();
        BigDecimal discount = BigDecimal.ZERO;
        // PRIMER LOOP: Validar y calcular total
        for (ProductPatchRequestDto productRequest : saleDetailsRequestDto.getProductsList()) {
            String name = productRequest.getName();
            Long quantity = productRequest.getQuantity();
            discount = (saleDetailsRequestDto.getDiscount() != null)
                    ? saleDetailsRequestDto.getDiscount()
                    : BigDecimal.ZERO;


            // Obtener producto y guardarlo en caché
            ProductResponseDto product = productService.getProductByName(name);
            productsCache.put(name, product);  // Guardar en cache

            if (quantity > product.getStock()) {
                log.error("Stock insufficient for: {}", name);
                throw new InsufficientResourcesException("Stock insufficient for: " + name);
            }

            BigDecimal totalForThisProduct = calculateItemPrice(product.getSalePrice(), quantity, discount);
            grandTotal = grandTotal.add(totalForThisProduct);
        }
        Sale sale = Sale.builder()
                .date(LocalDateTime.now())
                .amount(grandTotal)
                .build();
        sale = saleRepository.save(sale);

        // SEGUNDO LOOP: Crear SaleDetails usando caché
        List<SaleDetailsResponseDto> saleDetailsList = new ArrayList<>();
        for (ProductPatchRequestDto productRequest : saleDetailsRequestDto.getProductsList()) {
            String productName = productRequest.getName();
            Long productQuantity = productRequest.getQuantity();

            // Obtener del cache (no consulta adicional)
            ProductResponseDto currentProduct = productsCache.get(productName);

            // Actualizar stock
            currentProduct.setStock(currentProduct.getStock() - productQuantity);
            productService.updatePatchProductByName(productName, productMapper.convertResponseDtoToPatchDto(currentProduct));

            // Calcular precio y crear SaleDetails
            BigDecimal unitPriceWithDiscount = calculateItemPrice(currentProduct.getSalePrice(), productQuantity, discount);

            SaleDetails saleDetails = SaleDetails.builder()
                    .sale(sale)
                    .product(productMapper.convertResponseDtoToEntity(currentProduct))
                    .discount(discount)
                    .amount(unitPriceWithDiscount)
                    .quantity(productQuantity)
                    .build();

            SaleDetails savedSaleDetails = saleDetailsRepository.save(saleDetails);
            saleDetailsList.add(saleDetailsMapper.saleDetailsEntityToResponseDto(savedSaleDetails));
        }

        return SaleResponseDto.builder()
                .id(sale.getId())
                .date(sale.getDate())
                .amount(sale.getAmount())
                .saleDetailsResponseDto(saleDetailsList)
                .build();
    }

    private BigDecimal calculateItemPrice(BigDecimal unitPrice, Long quantity, BigDecimal discount) {
        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = unitPrice.multiply(discount);
            unitPrice = unitPrice.subtract(discountAmount);
        }
        return unitPrice.multiply(new BigDecimal(quantity));
    }

    public List<SaleResponseDto> getSalesByDate(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException();
        }
        List<Sale> saleList = saleRepository.findSalesWithDetailsAfter(start, end);
        if (saleList.isEmpty()) {
            log.info("There is not results with this parameters, start: {}, end: {}", start, end);
            return Collections.emptyList();
        }
        return saleList.stream().map(saleMapper::saleEntityToSaleResponseDto).toList();
    }
}