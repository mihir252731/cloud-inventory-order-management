package com.cloudinventory.inventory.integration.sap;

public record SapSyncResponse(
        String operation,
        int recordsProcessed,
        String sourceSystem,
        String statusMessage
) {
}
