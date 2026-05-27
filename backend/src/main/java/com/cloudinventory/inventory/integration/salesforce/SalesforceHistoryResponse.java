package com.cloudinventory.inventory.integration.salesforce;

import java.util.List;

public record SalesforceHistoryResponse(
        String customerEmail,
        String salesforceAccountId,
        List<String> recentOrders,
        List<String> recentActivities
) {
}
