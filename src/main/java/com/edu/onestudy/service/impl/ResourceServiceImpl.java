package com.edu.onestudy.service.impl;

import com.edu.onestudy.constant.Constant;
import com.edu.onestudy.dto.resource.GetResourceResponse;
import com.edu.onestudy.entity.Resource;
import com.edu.onestudy.entity.User;
import com.edu.onestudy.repository.ResourceRepository;
import com.edu.onestudy.service.ResourceService;
import com.edu.onestudy.thirdparty.storage.StorageFactory;
import com.edu.onestudy.thirdparty.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private final StorageService storageService;

    public ResourceServiceImpl(StorageFactory storageFactory, ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
        this.storageService = storageFactory.getStorageService(Constant.STORAGE_AWS);
    }

    @Override
    public Resource uploadResource(MultipartFile file, UUID requestId) {

        String url = storageService.uploadFile(file);

        Resource resource = Resource.builder()
                .name(file.getOriginalFilename())
                .url(url)
                .ownerId(requestId)
                .extension(file.getContentType())
                .sizeBytes(file.getSize())
                .cloudId(storageService.getProviderName())
                .build();
        int test = resource.getLastModifiedAt().getDayOfMonth();
        resourceRepository.save(resource);
        return resource;
    }

    @Override
    public GetResourceResponse getResource(String id, UUID requestId) {
        Resource resource = resourceRepository.findById(requestId.toString());

        if (Objects.isNull(resource)) {
            throw new RuntimeException("Resource not found");
        }

        try {
            if (!requestId.equals(resource.getOwnerId())) {
                throw new RuntimeException("You are not allowed to access this resource");
            }

            String url = storageService.getFileUrl(resource.getUrl());

            return GetResourceResponse.builder()
                    .name(resource.getName())
                    .url(url)
                    .build();

        } catch (Exception e) {
            log.info("Generate url failed {}", e.getMessage());
            throw new RuntimeException("Generate url failed");
        }
    }

    @Override
    public File downloadResourceFromUrl(String urlString) {
        try {
            URI uri = URI.create(urlString);
            URL url = uri.toURL();

            String fileName = Path.of(uri.getPath()).getFileName().toString();
            File outputFile = File.createTempFile("download_", "_" + fileName);

            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return outputFile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from URL: " + urlString, e);
        }
    }
}
