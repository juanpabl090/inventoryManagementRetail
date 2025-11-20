package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT DISTINCT s FROM Sale s JOIN FETCH s.saleDetails d WHERE s.date >= :start AND s.date <= :end")
    List<Sale> findSalesWithDetailsAfter(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}