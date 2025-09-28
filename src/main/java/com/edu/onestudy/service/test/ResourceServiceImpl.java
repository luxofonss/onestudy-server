package com.edu.onestudy.service.test;

import com.edu.onestudy.constant.Constant;
import com.edu.onestudy.dto.resource.GetResourceResponse;
import com.edu.onestudy.entity.Resource;
import com.edu.onestudy.repository.ResourceRepository;
import com.edu.onestudy.service.ResourceService;
import com.edu.onestudy.thirdparty.storage.StorageFactory;
import com.edu.onestudy.thirdparty.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceServiceImpl {

    private final ResourceRepository resourceRepository;

    private final StorageService storageService;

    public ResourceServiceImpl(StorageFactory storageFactory, ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
        this.storageService = storageFactory.getStorageService(Constant.STORAGE_AWS);
    }

    public Resource uploadResource(MultipartFile file, UUID requestId) {

        String url = storageService.uploadFile(file);
        List<Resource> lst = new ArrayList<>();
        List<String> test2 = lst.stream().map(Resource::getId).map(UUID::toString).collect(Collectors.toList());

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

}
