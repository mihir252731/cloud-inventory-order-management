package com.cloudinventory.inventory.integration.sap;

public record SapInventoryRecord(
        String sku,
        int availableQuantity,
        String sourceSystem,
        String materialCode
) {
}
