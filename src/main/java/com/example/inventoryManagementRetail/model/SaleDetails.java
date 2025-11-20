package com.example.inventoryManagementRetail.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "sales_details")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @JsonIgnore
    private Sale sale;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(precision = 8, scale = 2)
    private BigDecimal amount;
    @Column(precision = 8, scale = 2)
    private BigDecimal discount;
    private Long quantity;
}
