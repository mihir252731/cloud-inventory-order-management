package com.cloudinventory.inventory.integration.common;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class IntegrationSyncEventService {

    private final IntegrationSyncEventRepository integrationSyncEventRepository;

    public IntegrationSyncEventService(IntegrationSyncEventRepository integrationSyncEventRepository) {
        this.integrationSyncEventRepository = integrationSyncEventRepository;
    }

    public IntegrationSyncEvent record(
            ExternalSystemType externalSystem,
            SyncDirection direction,
            SyncStatus status,
            String operationName,
            String referenceId,
            String detailMessage
    ) {
        IntegrationSyncEvent event = new IntegrationSyncEvent();
        event.setExternalSystem(externalSystem);
        event.setDirection(direction);
        event.setStatus(status);
        event.setOperationName(operationName);
        event.setReferenceId(referenceId);
        event.setDetailMessage(detailMessage);
        return integrationSyncEventRepository.save(event);
    }

    public List<IntegrationSyncEvent> getRecentEvents() {
        return integrationSyncEventRepository.findTop20ByOrderByIdDesc();
    }

    public List<IntegrationSyncEvent> getRecentEvents(ExternalSystemType externalSystemType) {
        return integrationSyncEventRepository.findTop20ByExternalSystemOrderByIdDesc(externalSystemType);
    }
}
