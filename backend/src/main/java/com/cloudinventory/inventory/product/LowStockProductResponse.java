package com.cloudinventory.inventory.product;

public record LowStockProductResponse(
        Long id,
        String sku,
        String name,
        Integer stockQuantity,
        Integer reorderLevel,
        String supplierName
) {
    public static LowStockProductResponse from(Product product) {
        return new LowStockProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getStockQuantity(),
                product.getReorderLevel(),
                product.getSupplier().getName()
        );
    }
}
