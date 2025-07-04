package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleDetailsRepository extends JpaRepository<SaleDetails, Long> {
}