package com.example.inventoryManagementRetail.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "sales_details")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale_details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(precision = 8, scale = 2)
    private BigDecimal amount;
    @Column(precision = 8, scale = 2)
    private BigDecimal discount;

}
