package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    boolean existsByName(String name);

    Optional<ProductType> findByName(String name);
}
