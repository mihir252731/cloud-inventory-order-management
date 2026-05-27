package com.cloudinventory.inventory.integration.sap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integrations.sap")
public record SapProperties(
        String erpBaseUrl,
        String s4hanaBaseUrl,
        String inventoryPath,
        String purchaseOrderPath,
        boolean mockMode
) {
}
