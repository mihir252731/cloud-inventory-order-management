package com.cloudinventory.inventory.integration.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegrationSyncEventRepository extends JpaRepository<IntegrationSyncEvent, Long> {

    List<IntegrationSyncEvent> findTop20ByExternalSystemOrderByIdDesc(ExternalSystemType externalSystem);

    List<IntegrationSyncEvent> findTop20ByOrderByIdDesc();
}
