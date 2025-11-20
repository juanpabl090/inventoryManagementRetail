package com.example.inventoryManagementRetail.controller;

import com.example.inventoryManagementRetail.dto.SaleDetailsDto.SaleDetailsRequestDto;
import com.example.inventoryManagementRetail.dto.SalesDto.SaleResponseDto;
import com.example.inventoryManagementRetail.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/register")
    public ResponseEntity<SaleResponseDto> sale(@Valid @RequestBody SaleDetailsRequestDto saleDetailsRequestDto) {
        SaleResponseDto saleResponseDto = saleService.registerSale(saleDetailsRequestDto);
        return ResponseEntity.ok(saleResponseDto);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<SaleResponseDto>> getSaleByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        List<SaleResponseDto> saleResponseList = saleService.getSalesByDate(startDateTime, endDateTime);
        return ResponseEntity.ok(saleResponseList);
    }
}