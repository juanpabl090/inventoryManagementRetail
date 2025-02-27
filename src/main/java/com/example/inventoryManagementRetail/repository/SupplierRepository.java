package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}