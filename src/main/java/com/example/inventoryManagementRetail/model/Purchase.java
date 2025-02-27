package com.example.inventoryManagementRetail.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "purchases")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    private Long quantity;
    @Column(precision = 8, scale = 2)
    private BigDecimal amount;
    private LocalDateTime date;
}