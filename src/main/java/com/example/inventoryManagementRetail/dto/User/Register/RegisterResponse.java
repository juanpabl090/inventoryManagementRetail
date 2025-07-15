package com.example.inventoryManagementRetail.dto.User.Register;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private List<String> roles = new ArrayList<>();
}