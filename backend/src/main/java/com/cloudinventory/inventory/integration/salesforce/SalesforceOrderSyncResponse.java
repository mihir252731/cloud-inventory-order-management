package com.cloudinventory.inventory.integration.salesforce;

public record SalesforceOrderSyncResponse(
        String orderNumber,
        String salesforceOrderId,
        String status,
        String detailMessage
) {
}
