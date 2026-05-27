package com.cloudinventory.inventory.integration.sap;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinventory.inventory.integration.common.IntegrationSyncEvent;

@RestController
@RequestMapping("/api/integrations/sap")
public class SapIntegrationController {

    private final SapIntegrationService sapIntegrationService;

    public SapIntegrationController(SapIntegrationService sapIntegrationService) {
        this.sapIntegrationService = sapIntegrationService;
    }

    @GetMapping("/health")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SapHealthResponse getHealth() {
        return sapIntegrationService.getHealth();
    }

    @PostMapping("/inventory-sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SapSyncResponse syncInventory() {
        return sapIntegrationService.syncInventoryLevels();
    }

    @PostMapping("/purchase-orders/sync")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SapSyncResponse syncPurchaseOrders() {
        return sapIntegrationService.syncPurchaseOrders();
    }

    @GetMapping("/events")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<IntegrationSyncEvent> getRecentEvents() {
        return sapIntegrationService.getRecentEvents();
    }
}
