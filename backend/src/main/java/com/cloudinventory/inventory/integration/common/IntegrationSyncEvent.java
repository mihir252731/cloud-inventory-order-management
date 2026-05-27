package com.cloudinventory.inventory.integration.common;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "integration_sync_events")
public class IntegrationSyncEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExternalSystemType externalSystem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncDirection direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncStatus status;

    @Column(nullable = false)
    private String operationName;

    @Column(nullable = false)
    private String referenceId;

    @Column(nullable = false, length = 4000)
    private String detailMessage;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public ExternalSystemType getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(ExternalSystemType externalSystem) {
        this.externalSystem = externalSystem;
    }

    public SyncDirection getDirection() {
        return direction;
    }

    public void setDirection(SyncDirection direction) {
        this.direction = direction;
    }

    public SyncStatus getStatus() {
        return status;
    }

    public void setStatus(SyncStatus status) {
        this.status = status;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
