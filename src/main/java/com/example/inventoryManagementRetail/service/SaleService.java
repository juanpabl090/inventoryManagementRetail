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
import java.util.Objects;
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
            // 1. Preparar datos iniciales
            Map<String, Long> productCountMap = saleDetailsRequestDto.getProductsList().stream().collect(Collectors.groupingBy(ProductPatchRequestDto::getName, Collectors.counting()));
            List<SaleDetails> saleDetailsToSave = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            // 2. Primera pasada: Validar stock y calcular total
            for (Map.Entry<String, Long> entry : productCountMap.entrySet()) {
                String productName = entry.getKey();
                Long quantity = entry.getValue();
                ProductResponseDto productInStock = productService.getProductByName(productName);

                if (quantity > productInStock.getStock()) {
                    log.error("Stock insufficient");
                    throw new InsufficientResourcesException("Stock insufficient");
                }

                BigDecimal itemPrice = calculateItemPrice(productInStock, saleDetailsRequestDto.getDiscount());
                totalAmount = totalAmount.add(itemPrice.multiply(new BigDecimal(quantity)));
            }
            // 3. Crear y guardar Sale primero
            Sale sale = Sale.builder()
                    .date(LocalDateTime.now(ZoneId.of("UTC")))
                    .amount(totalAmount)
                    .build();
            sale = saleRepository.save(sale);
            // 4. Segunda pasada: Actualizar productos y crear detalles
            for (Map.Entry<String, Long> entry : productCountMap.entrySet()) {
                String productName = entry.getKey();
                Long quantity = entry.getValue();

                ProductResponseDto productInStock = productService.getProductByName(productName);
                BigDecimal itemPrice = calculateItemPrice(productInStock, saleDetailsRequestDto.getDiscount());

                // Actualizar stock del producto
                productInStock.setStock(productInStock.getStock() - quantity);
                ProductPatchRequestDto productPatchRequestDto = productMapper.convertResponseDtoToPatchDto(productInStock);
                ProductResponseDto updatedProduct = productService.updatePatchProductByName(productName, productPatchRequestDto);

                for (int i = 0; i < quantity; i++) {
                    // Generar detalles de venta (uno por unidad)
                    SaleDetails saleDetails = SaleDetails.builder()
                            .sale(sale)
                            .product(productMapper.convertResponseDtoToEntity(updatedProduct))
                            .amount(itemPrice)
                            .discount((saleDetailsRequestDto.getDiscount() != null) ? saleDetailsRequestDto.getDiscount() : BigDecimal.ZERO)
                            .quantity(1L)
                            .build();
                    saleDetailsToSave.add(saleDetails);
                }
            }
            // 5. Guardar todos los detalles de venta
            List<SaleDetails> savedDetails = saleDetailsRepository.saveAll(saleDetailsToSave);
            // 6. Construir respuesta
            return SaleResponseDto.builder()
                    .id(sale.getId())
                    .date(sale.getDate())
                    .amount(sale.getAmount())
                    .saleDetailsResponseDto(saleDetailsMapper.convertSaleDetailsListToSaleDetailsResponseDtoList(savedDetails))
                    .build();
        } catch (DataAccessException e) {
            log.error("Error registering sale: {}", e.getMessage());
            throw new RuntimeException("Error registering sale", e);
        }
    }

    private BigDecimal calculateItemPrice(ProductResponseDto productInStock, BigDecimal discount) {
        if (discount != null && Objects.equals(discount, BigDecimal.ZERO)) {
            BigDecimal discountAmount = productInStock.getSalePrice().multiply(discount);
            return productInStock.getSalePrice().subtract(discountAmount);
        }
        return productInStock.getSalePrice();
    }
}