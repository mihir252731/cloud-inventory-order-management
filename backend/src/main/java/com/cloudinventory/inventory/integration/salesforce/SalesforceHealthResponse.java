package com.cloudinventory.inventory.integration.salesforce;

public record SalesforceHealthResponse(
        String integrationName,
        String baseUrl,
        boolean mockMode,
        String status
) {
}
