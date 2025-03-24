package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    void deleteByName(String name);
}