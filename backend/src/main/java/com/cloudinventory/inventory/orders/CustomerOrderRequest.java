package com.cloudinventory.inventory.orders;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CustomerOrderRequest(
        @NotBlank String customerName,
        @Email @NotBlank String customerEmail,
        @Valid @NotEmpty List<OrderLineRequest> items
) {
}
