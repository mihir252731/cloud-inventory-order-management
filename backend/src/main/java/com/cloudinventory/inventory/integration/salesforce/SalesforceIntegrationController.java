package com.cloudinventory.inventory.integration.salesforce;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinventory.inventory.integration.common.IntegrationSyncEvent;

@RestController
@RequestMapping("/api/integrations/salesforce")
public class SalesforceIntegrationController {

    private final SalesforceIntegrationService salesforceIntegrationService;

    public SalesforceIntegrationController(SalesforceIntegrationService salesforceIntegrationService) {
        this.salesforceIntegrationService = salesforceIntegrationService;
    }

    @GetMapping("/health")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesforceHealthResponse getHealth() {
        return salesforceIntegrationService.getHealth();
    }

    @PostMapping("/orders/{orderId}/push")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesforceOrderSyncResponse pushOrder(@PathVariable Long orderId) {
        return salesforceIntegrationService.pushOrder(orderId);
    }

    @GetMapping("/history/{customerEmail}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesforceHistoryResponse getHistory(@PathVariable String customerEmail) {
        return salesforceIntegrationService.getCustomerHistory(customerEmail);
    }

    @GetMapping("/events")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<IntegrationSyncEvent> getRecentEvents() {
        return salesforceIntegrationService.getRecentEvents();
    }
}
