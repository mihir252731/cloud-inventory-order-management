package com.cloudinventory.inventory.integration.sap;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinventory.inventory.integration.common.ExternalSystemType;
import com.cloudinventory.inventory.integration.common.IntegrationSyncEvent;
import com.cloudinventory.inventory.integration.common.IntegrationSyncEventService;
import com.cloudinventory.inventory.integration.common.SyncDirection;
import com.cloudinventory.inventory.integration.common.SyncStatus;
import com.cloudinventory.inventory.orders.PurchaseOrder;
import com.cloudinventory.inventory.orders.PurchaseOrderRepository;
import com.cloudinventory.inventory.orders.PurchaseOrderStatus;
import com.cloudinventory.inventory.product.Product;
import com.cloudinventory.inventory.product.ProductRepository;
import com.cloudinventory.inventory.supplier.Supplier;
import com.cloudinventory.inventory.supplier.SupplierRepository;

@Service
public class SapIntegrationService {

    private final SapProperties sapProperties;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final IntegrationSyncEventService integrationSyncEventService;

    public SapIntegrationService(
            SapProperties sapProperties,
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            IntegrationSyncEventService integrationSyncEventService
    ) {
        this.sapProperties = sapProperties;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.integrationSyncEventService = integrationSyncEventService;
    }

    public SapHealthResponse getHealth() {
        return new SapHealthResponse(
                "SAP ERP / SAP S/4HANA OData",
                sapProperties.erpBaseUrl(),
                sapProperties.s4hanaBaseUrl(),
                sapProperties.mockMode(),
                sapProperties.mockMode() ? "MOCK_CONNECTED" : "CONFIGURED"
        );
    }

    @Transactional
    public SapSyncResponse syncInventoryLevels() {
        List<SapInventoryRecord> records = mockInventoryRecords();
        for (SapInventoryRecord record : records) {
            productRepository.findBySku(record.sku()).ifPresent(product -> {
                product.setStockQuantity(record.availableQuantity());
                productRepository.save(product);
                integrationSyncEventService.record(
                        ExternalSystemType.SAP_S4HANA,
                        SyncDirection.INBOUND,
                        SyncStatus.MOCK_SUCCESS,
                        "inventory-sync",
                        record.materialCode(),
                        "Updated " + product.getSku() + " from " + record.sourceSystem() + " OData inventory feed."
                );
            });
        }

        return new SapSyncResponse(
                "inventory-sync",
                records.size(),
                "SAP ERP / SAP S/4HANA",
                "Inventory records synchronized into internal stock tables"
        );
    }

    @Transactional
    public SapSyncResponse syncPurchaseOrders() {
        List<SapPurchaseOrderRecord> records = mockPurchaseOrderRecords();
        for (SapPurchaseOrderRecord record : records) {
            boolean exists = purchaseOrderRepository.findAll().stream()
                    .anyMatch(po -> po.getPoNumber().equalsIgnoreCase(record.poNumber()));
            if (!exists) {
                Supplier supplier = supplierRepository.findAll().stream()
                        .filter(item -> item.getName().equalsIgnoreCase(record.supplierName()))
                        .findFirst()
                        .orElseGet(() -> {
                            Supplier newSupplier = new Supplier();
                            newSupplier.setName(record.supplierName());
                            newSupplier.setContactName("ERP Supplier Contact");
                            newSupplier.setEmail("erp-" + record.supplierName().toLowerCase().replace(" ", "-") + "@example.com");
                            newSupplier.setPhone("+1-800-555-0199");
                            newSupplier.setCity("Chicago");
                            newSupplier.setCountry("USA");
                            newSupplier.setLeadTime("5 business days");
                            return supplierRepository.save(newSupplier);
                        });

                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setPoNumber(record.poNumber());
                purchaseOrder.setSupplier(supplier);
                purchaseOrder.setRequestedBy("SAP Sync Service");
                purchaseOrder.setStatus(PurchaseOrderStatus.valueOf(record.status()));
                purchaseOrder.setTotalAmount(record.totalAmount());
                purchaseOrder.setNotes("Imported from " + record.sourceSystem() + " purchase order feed.");
                purchaseOrderRepository.save(purchaseOrder);
            }

            integrationSyncEventService.record(
                    ExternalSystemType.SAP_ERP,
                    SyncDirection.INBOUND,
                    SyncStatus.MOCK_SUCCESS,
                    "purchase-order-sync",
                    record.poNumber(),
                    "Imported purchase order " + record.poNumber() + " from " + record.sourceSystem() + "."
            );
        }

        return new SapSyncResponse(
                "purchase-order-sync",
                records.size(),
                "SAP ERP",
                "Purchase order records synchronized from ERP into the platform"
        );
    }

    public List<IntegrationSyncEvent> getRecentEvents() {
        return integrationSyncEventService.getRecentEvents(ExternalSystemType.SAP_ERP);
    }

    private List<SapInventoryRecord> mockInventoryRecords() {
        return List.of(
                new SapInventoryRecord("LAP-1001", 55, "SAP S/4HANA", "MAT-1001"),
                new SapInventoryRecord("MON-2001", 24, "SAP ERP", "MAT-2001"),
                new SapInventoryRecord("BAR-6100", 11, "SAP S/4HANA", "MAT-6100")
        );
    }

    private List<SapPurchaseOrderRecord> mockPurchaseOrderRecords() {
        return List.of(
                new SapPurchaseOrderRecord("PO-SAP-3001", "TechSupply Inc.", new BigDecimal("8500.00"), "APPROVED", "SAP ERP"),
                new SapPurchaseOrderRecord("PO-S4-3002", "Global Parts Co.", new BigDecimal("4200.00"), "IN_TRANSIT", "SAP S/4HANA")
        );
    }
}
