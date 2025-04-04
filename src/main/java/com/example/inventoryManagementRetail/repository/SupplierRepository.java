package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}