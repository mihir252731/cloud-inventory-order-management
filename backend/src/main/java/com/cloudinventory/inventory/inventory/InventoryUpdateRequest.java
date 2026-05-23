package com.cloudinventory.inventory.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryUpdateRequest(
        @NotNull Long productId,
        @NotNull InventoryTransactionType transactionType,
        @NotNull @Min(1) Integer quantity,
        @NotBlank String performedBy,
        String notes
) {
}
