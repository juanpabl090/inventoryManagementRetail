package com.example.inventoryManagementRetail.service;

import com.example.inventoryManagementRetail.dto.ProductDto.ProductPatchRequestDto;
import com.example.inventoryManagementRetail.dto.ProductDto.ProductResponseDto;
import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsRequestDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.exception.InsufficientResourcesException;
import com.example.inventoryManagementRetail.mapper.ProductMapper;
import com.example.inventoryManagementRetail.mapper.SaleDetailsMapper;
import com.example.inventoryManagementRetail.model.Sale;
import com.example.inventoryManagementRetail.model.SaleDetails;
import com.example.inventoryManagementRetail.repository.SaleDetailsRepository;
import com.example.inventoryManagementRetail.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailsRepository saleDetailsRepository;
    private final SaleDetailsMapper saleDetailsMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;


    public SaleService(SaleRepository saleRepository, SaleDetailsRepository saleDetailsRepository, SaleDetailsMapper saleDetailsMapper, ProductService productService, ProductMapper productMapper) {
        this.saleRepository = saleRepository;
        this.saleDetailsRepository = saleDetailsRepository;
        this.saleDetailsMapper = saleDetailsMapper;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    private static Sale getBuildSale(BigDecimal totalWithDiscount) {
        return Sale.builder()
                .date(LocalDateTime.now(ZoneId.of("UTC")))
                .amount(totalWithDiscount)
                .build();
    }

    private static BigDecimal getDiscount(SaleDetailsRequestDto saleDetailsRequestDto, ProductResponseDto productInStock, BigDecimal totalWithDiscount) {
        BigDecimal discount = productInStock.getSalePrice().multiply(saleDetailsRequestDto.getDiscount());
        BigDecimal priceWithDiscount = productInStock.getSalePrice();
        priceWithDiscount = priceWithDiscount.subtract(discount);
        totalWithDiscount = totalWithDiscount.add(priceWithDiscount);
        return totalWithDiscount;
    }

    private static BigDecimal getTotalWithOutDiscount(ProductResponseDto productInStock) {
        BigDecimal totalWithoutDiscount = BigDecimal.ZERO;
        totalWithoutDiscount = totalWithoutDiscount.add(productInStock.getSalePrice());
        return totalWithoutDiscount;
    }

    private static ProductResponseDto getProductResponseDto(ProductResponseDto productResponseDto) {
        ProductResponseDto productsUpdated;
        productsUpdated = ProductResponseDto.builder()
                .id(productResponseDto.getId())
                .name(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .categoryId(productResponseDto.getCategoryId())
                .buyPrice(productResponseDto.getBuyPrice())
                .salePrice(productResponseDto.getSalePrice())
                .stock(productResponseDto.getStock())
                .createdDate(productResponseDto.getCreatedDate())
                .updatedDate(productResponseDto.getUpdatedDate())
                .supplierId(productResponseDto.getSupplierId())
                .productTypeId(productResponseDto.getProductTypeId())
                .build();
        return productsUpdated;
    }

    /**
     * Obtener él sale y saleDetails por separado del SaleRequestDto <br>
     * Actualizar el producto o productos <br>
     * mapper el response de sale y saleDetails <br>
     * registrar el sale <br>
     * registrar el saleDetails <br>
     */
    @Transactional
    public SaleResponseDto registerSale(SaleDetailsRequestDto saleDetailsRequestDto) {
        try {
            // initialize variables
            Sale sale;
            ProductResponseDto productUpdated = ProductResponseDto.builder().build();
            List<ProductResponseDto> productsUpdated = new ArrayList<>();
            List<SaleDetails> saleDetailsList = new ArrayList<>();
            BigDecimal totalWithDiscount = BigDecimal.ZERO;
            ProductResponseDto.builder().build();
            ProductResponseDto productInStock = ProductResponseDto.builder().build();
            long quantity = 0;
            Map<String, Long> productCountMap = saleDetailsRequestDto.getProductsList().stream().collect(Collectors.groupingBy(ProductPatchRequestDto::getName, Collectors.counting()));

            // iteration and handle of each product
            for (Map.Entry<String, Long> entry : productCountMap.entrySet()) {
                String productName = entry.getKey();
                productInStock = productService.getProductByName(productName);
                quantity = entry.getValue();
                if (quantity > productInStock.getStock()) {
                    log.error("Insufficient stock for product: {}", productName);
                    throw new InsufficientResourcesException("Insufficient stock for product: " + productName);
                }
                if (saleDetailsRequestDto.getDiscount() != null && saleDetailsRequestDto.getDiscount().compareTo(BigDecimal.ZERO) > 0.00) {
                    totalWithDiscount = getDiscount(saleDetailsRequestDto, productInStock, totalWithDiscount);
                }
                productInStock.setStock(productInStock.getStock() - (int) quantity);
                ProductPatchRequestDto productPatchRequestDto = productMapper.convertResponseDtoToPatchDto(productInStock);
                ProductResponseDto productResponseDto = productService.updatePatchProductByName(productName, productPatchRequestDto);
                productUpdated = getProductResponseDto(productResponseDto);
                productsUpdated.add(productUpdated);
            }
            // save Sale
            sale = getBuildSale((totalWithDiscount.equals(BigDecimal.ZERO)) ? getTotalWithOutDiscount(productInStock) : BigDecimal.ZERO);
            sale = saleRepository.save(sale);
            // iterate each productUpdate, build saleDetails and save it
            for (int i = 0; i < productsUpdated.size(); i++) {
                SaleDetails saleDetails = getSaleDetailsResponseDto(saleDetailsRequestDto, productUpdated, quantity,
                        (totalWithDiscount.equals(BigDecimal.ZERO)) ? getTotalWithOutDiscount(productInStock) : BigDecimal.ZERO);
                saleDetails = saleDetailsRepository.save(saleDetails);
                saleDetailsList.add(saleDetails);
            }
            log.info("Sale details registered successfully");
            return getBuildSaleResponseDto(sale, saleDetailsList);
        } catch (DataAccessException e) {
            log.error("Error registering sale: {}", e.getMessage());
            throw new RuntimeException("Error registering sale", e);
        }
    }

    private SaleDetails getSaleDetailsResponseDto(SaleDetailsRequestDto saleDetailsRequestDto, ProductResponseDto productsUpdated, Long quantity, BigDecimal totalWithDiscount) {
        return SaleDetails.builder()
                .product(productMapper.convertResponseDtoToEntity(productsUpdated))
                .amount(totalWithDiscount)
                .discount(saleDetailsRequestDto.getDiscount())
                .quantity(quantity)
                .build();
    }

    private SaleResponseDto getBuildSaleResponseDto(Sale sale, List<SaleDetails> saleDetails) {
        return SaleResponseDto.builder()
                .id(sale.getId())
                .date(sale.getDate())
                .amount(sale.getAmount())
                .saleDetailsResponseDto(saleDetailsMapper.convertSaleDetailsListToSaleDetailsResponseDtoList(saleDetails))
                .build();
    }
}