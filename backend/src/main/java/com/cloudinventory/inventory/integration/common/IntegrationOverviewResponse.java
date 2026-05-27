package com.cloudinventory.inventory.integration.common;

import java.util.List;

public record IntegrationOverviewResponse(
        String projectName,
        boolean sapMockMode,
        boolean salesforceMockMode,
        List<IntegrationSyncEvent> recentEvents
) {
}
