package com.example.inventoryManagementRetail.dto.User.Register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "userName cannot be blank")
    private String userName;
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private List<String> roles = new ArrayList<>();
}
