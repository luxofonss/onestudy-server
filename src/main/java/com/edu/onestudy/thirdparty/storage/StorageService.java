package com.edu.onestudy.thirdparty.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
public abstract class StorageService {

    public abstract String getFileUrl(String fileName);

    public abstract String uploadFile(MultipartFile file);

    public abstract String getProviderName();

    public abstract byte[] downloadFile(String fileName);

    public abstract String deleteFile(String fileName);

    protected File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String name = originalFilename;
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex != -1) {
            name = originalFilename.substring(0, lastDotIndex);
            extension = originalFilename.substring(lastDotIndex);
        }

        Path tempFilePath = Files.createTempFile(name + "_", extension);

        file.transferTo(tempFilePath.toFile());

        log.debug("Created temporary file: {}", tempFilePath.toAbsolutePath());
        return tempFilePath.toFile();
    }
}
