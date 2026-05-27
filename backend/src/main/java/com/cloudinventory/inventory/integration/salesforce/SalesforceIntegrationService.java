package com.cloudinventory.inventory.integration.salesforce;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudinventory.inventory.customer.CustomerAccount;
import com.cloudinventory.inventory.customer.CustomerAccountService;
import com.cloudinventory.inventory.integration.common.ExternalSystemType;
import com.cloudinventory.inventory.integration.common.IntegrationSyncEvent;
import com.cloudinventory.inventory.integration.common.IntegrationSyncEventService;
import com.cloudinventory.inventory.integration.common.SyncDirection;
import com.cloudinventory.inventory.integration.common.SyncStatus;
import com.cloudinventory.inventory.orders.CustomerOrder;
import com.cloudinventory.inventory.orders.CustomerOrderRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SalesforceIntegrationService {

    private final SalesforceProperties salesforceProperties;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerAccountService customerAccountService;
    private final IntegrationSyncEventService integrationSyncEventService;

    public SalesforceIntegrationService(
            SalesforceProperties salesforceProperties,
            CustomerOrderRepository customerOrderRepository,
            CustomerAccountService customerAccountService,
            IntegrationSyncEventService integrationSyncEventService
    ) {
        this.salesforceProperties = salesforceProperties;
        this.customerOrderRepository = customerOrderRepository;
        this.customerAccountService = customerAccountService;
        this.integrationSyncEventService = integrationSyncEventService;
    }

    public SalesforceHealthResponse getHealth() {
        return new SalesforceHealthResponse(
                "Salesforce CRM",
                salesforceProperties.baseUrl(),
                salesforceProperties.mockMode(),
                salesforceProperties.mockMode() ? "MOCK_CONNECTED" : "CONFIGURED"
        );
    }

    public SalesforceOrderSyncResponse pushOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
        CustomerAccount customerAccount = customerAccountService.getOrCreate(order.getCustomerName(), order.getCustomerEmail());
        String salesforceOrderId = "SF-ORD-" + order.getOrderNumber();

        integrationSyncEventService.record(
                ExternalSystemType.SALESFORCE_CRM,
                SyncDirection.OUTBOUND,
                SyncStatus.MOCK_SUCCESS,
                "salesforce-order-push",
                order.getOrderNumber(),
                "Pushed customer order " + order.getOrderNumber() + " for account " + customerAccount.getSalesforceAccountId() + " into Salesforce REST APIs."
        );

        return new SalesforceOrderSyncResponse(
                order.getOrderNumber(),
                salesforceOrderId,
                "SYNCED",
                "Customer order pushed to Salesforce CRM and linked to account " + customerAccount.getSalesforceAccountId()
        );
    }

    public SalesforceHistoryResponse getCustomerHistory(String customerEmail) {
        CustomerAccount customerAccount = customerAccountService.getOrCreate("CRM Customer", customerEmail);
        List<String> recentOrders = customerOrderRepository.findAll().stream()
                .filter(order -> order.getCustomerEmail().equalsIgnoreCase(customerEmail))
                .map(order -> order.getOrderNumber() + " - " + order.getStatus())
                .toList();

        integrationSyncEventService.record(
                ExternalSystemType.SALESFORCE_CRM,
                SyncDirection.INBOUND,
                SyncStatus.MOCK_SUCCESS,
                "salesforce-history-fetch",
                customerEmail,
                "Fetched customer and order history from Salesforce using SOQL-style lookup patterns."
        );

        return new SalesforceHistoryResponse(
                customerEmail,
                customerAccount.getSalesforceAccountId(),
                recentOrders,
                List.of(
                        "Open opportunity reviewed by account executive",
                        "Service renewal scheduled in Salesforce",
                        "Order status workflow updated via Apex automation"
                )
        );
    }

    public List<IntegrationSyncEvent> getRecentEvents() {
        return integrationSyncEventService.getRecentEvents(ExternalSystemType.SALESFORCE_CRM);
    }
}
