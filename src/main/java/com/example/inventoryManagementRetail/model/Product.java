package com.example.inventoryManagementRetail.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "products")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(precision = 8, scale = 2)
    private BigDecimal buy_price;
    @Column(precision = 8, scale = 2)
    private BigDecimal sale_price;
    private Long stock;
    private LocalDateTime created_date;
    private LocalDateTime updated_date;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private Product_type product_type;
}