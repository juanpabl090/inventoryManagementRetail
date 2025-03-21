package com.example.inventoryManagementRetail.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "contacts")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String email;
    private String address;
}