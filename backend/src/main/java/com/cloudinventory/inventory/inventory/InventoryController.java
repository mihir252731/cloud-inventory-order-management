package com.cloudinventory.inventory.inventory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinventory.inventory.product.LowStockProductResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public InventoryTransaction updateInventory(@Valid @RequestBody InventoryUpdateRequest request) {
        return inventoryService.updateInventory(request);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public List<LowStockProductResponse> getLowStockProducts() {
        return inventoryService.getLowStockProducts();
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_USER')")
    public List<InventoryTransaction> getRecentTransactions() {
        return inventoryService.getRecentTransactions();
    }
}
