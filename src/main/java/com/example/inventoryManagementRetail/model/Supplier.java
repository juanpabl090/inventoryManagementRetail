package com.example.inventoryManagementRetail.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "suppliers")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contact;
}