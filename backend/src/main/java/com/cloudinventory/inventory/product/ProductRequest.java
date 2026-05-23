package com.cloudinventory.inventory.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String category,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal unitPrice,
        @NotNull @Min(0) Integer stockQuantity,
        @NotNull @Min(0) Integer reorderLevel,
        @NotBlank String warehouseLocation,
        @NotNull Long supplierId
) {
}
