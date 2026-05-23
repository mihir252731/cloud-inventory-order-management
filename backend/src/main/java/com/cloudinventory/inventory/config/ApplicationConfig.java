package com.cloudinventory.inventory.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class ApplicationConfig {
}
