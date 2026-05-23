package com.cloudinventory.inventory.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(
        @NotBlank String name,
        @NotBlank String contactName,
        @Email @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String city,
        @NotBlank String country,
        String leadTime
) {
}
