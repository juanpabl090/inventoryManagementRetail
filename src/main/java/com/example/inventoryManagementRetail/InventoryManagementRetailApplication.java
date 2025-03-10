package com.example.inventoryManagementRetail;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementRetailApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementRetailApplication.class, args);
    }
    //TODO:Add loggers
    //TODO:Improve services and repositories

    @Override
    public void run(String... args) throws Exception {
        //TODO:add data
    }
}