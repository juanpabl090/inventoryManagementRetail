package com.example.inventoryManagementRetail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementRetailApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementRetailApplication.class, args);
        //TODO: Revisar porque no esta bien el amount en saleDetail (precio sin descuento por cada producto)
        //TODO: revisar si retorna bien el amount en sale (precio total de todos lor productos con descuento incluido)
    }
}