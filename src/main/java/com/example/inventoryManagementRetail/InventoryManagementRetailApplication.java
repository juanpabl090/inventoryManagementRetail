package com.example.inventoryManagementRetail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementRetailApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementRetailApplication.class, args);
    }

    //DONE: improve SOLID principles in supplier's classes and Contact´s Classes
    //DONE: fix the null problem in the supplier table in BD
    //DONE: normalize the endpoints of every controller
    //TODO: GET test the endpoints of product, category, etc.
    //TODO: Cada tipo de producto debe tener un nombre único.
    //TODO: normalize the methods in the services (update, delete, get by id and name, etc)
}