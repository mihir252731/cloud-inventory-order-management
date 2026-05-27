package com.cloudinventory.inventory.integration.common;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinventory.inventory.integration.salesforce.SalesforceProperties;
import com.cloudinventory.inventory.integration.sap.SapProperties;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationOverviewController {

    private final SapProperties sapProperties;
    private final SalesforceProperties salesforceProperties;
    private final IntegrationSyncEventService integrationSyncEventService;

    public IntegrationOverviewController(
            SapProperties sapProperties,
            SalesforceProperties salesforceProperties,
            IntegrationSyncEventService integrationSyncEventService
    ) {
        this.sapProperties = sapProperties;
        this.salesforceProperties = salesforceProperties;
        this.integrationSyncEventService = integrationSyncEventService;
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public IntegrationOverviewResponse getOverview() {
        return new IntegrationOverviewResponse(
                "Enterprise Inventory & Customer Order Management Platform",
                sapProperties.mockMode(),
                salesforceProperties.mockMode(),
                integrationSyncEventService.getRecentEvents()
        );
    }
}
