package com.cloudinventory.inventory.integration.sap;

public record SapHealthResponse(
        String integrationName,
        String erpBaseUrl,
        String s4hanaBaseUrl,
        boolean mockMode,
        String status
) {
}
