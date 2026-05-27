package com.cloudinventory.inventory.integration.salesforce;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integrations.salesforce")
public record SalesforceProperties(
        String baseUrl,
        String ordersPath,
        String accountsPath,
        String historyPath,
        boolean mockMode
) {
}
