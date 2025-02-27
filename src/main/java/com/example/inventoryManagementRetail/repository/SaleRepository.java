package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}