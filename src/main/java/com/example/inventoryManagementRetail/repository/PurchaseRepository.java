package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
