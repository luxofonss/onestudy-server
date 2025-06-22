package com.edu.onestudy.thirdparty.storage;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StorageFactory {

    private final List<StorageService> services;

    public StorageFactory(List<StorageService> services) {
        this.services = services;
    }

    public StorageService getStorageService(String providerName) {
        return services.stream()
                .filter(service -> service.getProviderName().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No storage service found for provider: " + providerName));
    }
}
