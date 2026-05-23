package com.cloudinventory.inventory.dashboard;

public record DashboardSummaryResponse(
        long totalProducts,
        long totalSuppliers,
        int totalStockUnits,
        long lowStockItems,
        long openCustomerOrders,
        long pendingPurchaseOrders,
        long activeUsers
) {
}
