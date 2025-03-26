package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> getAllProductsByCategory(Long categoryId);
    boolean existsByName(String name);
}