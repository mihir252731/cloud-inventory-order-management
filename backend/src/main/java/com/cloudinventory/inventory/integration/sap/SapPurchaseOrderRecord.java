package com.cloudinventory.inventory.integration.sap;

import java.math.BigDecimal;

public record SapPurchaseOrderRecord(
        String poNumber,
        String supplierName,
        BigDecimal totalAmount,
        String status,
        String sourceSystem
) {
}
