package com.example.inventoryManagementRetail.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "suppliers")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;
}