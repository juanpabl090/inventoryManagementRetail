package com.example.inventoryManagementRetail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InventoryManagementRetailApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementRetailApplication.class, args);
    }
}