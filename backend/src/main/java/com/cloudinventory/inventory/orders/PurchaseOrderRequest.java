package com.cloudinventory.inventory.orders;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseOrderRequest(
        @NotNull Long supplierId,
        @NotBlank String requestedBy,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal totalAmount,
        String notes
) {
}
