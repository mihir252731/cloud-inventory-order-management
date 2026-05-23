package com.cloudinventory.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CloudInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudInventoryApplication.class, args);
    }
}
