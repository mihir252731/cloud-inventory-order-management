package com.cloudinventory.inventory.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cloudinventory.inventory.integration.salesforce.SalesforceProperties;
import com.cloudinventory.inventory.integration.sap.SapProperties;

@Configuration
@EnableConfigurationProperties({CorsProperties.class, SapProperties.class, SalesforceProperties.class})
public class ApplicationConfig {
}
