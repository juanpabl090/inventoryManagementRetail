package com.example.inventoryManagementRetail.dto.User.Me;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MeResponse {
    private String userName;
    private List<String> roles;
    private String message;
}