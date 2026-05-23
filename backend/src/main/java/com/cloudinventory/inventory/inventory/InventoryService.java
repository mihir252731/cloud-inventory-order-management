package com.cloudinventory.inventory.inventory;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinventory.inventory.product.LowStockProductResponse;
import com.cloudinventory.inventory.product.Product;
import com.cloudinventory.inventory.product.ProductRepository;
import com.cloudinventory.inventory.product.ProductService;

@Service
public class InventoryService {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    public InventoryService(
            ProductService productService,
            ProductRepository productRepository,
            InventoryTransactionRepository inventoryTransactionRepository
    ) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    @Transactional
    @CacheEvict(value = {"products", "lowStockProducts"}, allEntries = true)
    public InventoryTransaction updateInventory(InventoryUpdateRequest request) {
        Product product = productService.getProductById(request.productId());
        int currentStock = product.getStockQuantity();
        int adjustedQuantity = switch (request.transactionType()) {
            case STOCK_IN, PURCHASE_ORDER_RECEIPT, ADJUSTMENT -> currentStock + request.quantity();
            case STOCK_OUT, CUSTOMER_ORDER_ALLOCATION -> currentStock - request.quantity();
        };

        if (adjustedQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock for product " + product.getName());
        }

        product.setStockQuantity(adjustedQuantity);
        productRepository.save(product);

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setTransactionType(request.transactionType());
        transaction.setQuantityChange(request.transactionType() == InventoryTransactionType.STOCK_OUT
                || request.transactionType() == InventoryTransactionType.CUSTOMER_ORDER_ALLOCATION
                ? -request.quantity()
                : request.quantity());
        transaction.setResultingStock(adjustedQuantity);
        transaction.setReferenceType(request.transactionType().name());
        transaction.setPerformedBy(request.performedBy());
        transaction.setNotes(request.notes());
        return inventoryTransactionRepository.save(transaction);
    }

    public List<LowStockProductResponse> getLowStockProducts() {
        return productService.getLowStockProducts();
    }

    public List<InventoryTransaction> getRecentTransactions() {
        return inventoryTransactionRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
