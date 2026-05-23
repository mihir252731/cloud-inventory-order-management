package com.cloudinventory.inventory.auth;

import com.cloudinventory.inventory.user.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank String username,
        @Email @NotBlank String email,
        @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotNull RoleName role
) {
}
