package com.edu.onestudy.thirdparty.storage;

import com.edu.onestudy.constant.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID; // Import for generating unique file names

@Component("googleStorageService")
@Slf4j
public class GoogleStorageServiceImpl extends StorageService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.bucket.domain}")
    private String domain;

    @Value("${gcp.credentials.file}")
    private String gcpCredentials;

    // No S3Client in constructor
    public GoogleStorageServiceImpl() {

    }

    @Override
    public String getFileUrl(String fileName) {
        return String.format("%s/%s", domain, fileName);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        File tempFile = null;
        String uploadedFileName = null;
        try {
            // Validate file presence
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Cannot upload empty file.");
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                fileExtension = originalFilename.substring(dotIndex);
            }
            // Use UUID for robust unique naming
            uploadedFileName = UUID.randomUUID().toString() + fileExtension;

            // Convert MultipartFile to a temporary File
            tempFile = File.createTempFile("upload-", fileExtension);
            file.transferTo(tempFile);

            InputStream inputStream = new ClassPathResource(gcpCredentials).getInputStream();

            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
            BlobId blobId = BlobId.of(bucketName, uploadedFileName);
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            storage.createFrom(blobInfo, Files.newInputStream(tempFile.toPath()));

            log.info("File {} uploaded to Google Cloud Storage bucket {} as {}", originalFilename, bucketName, uploadedFileName);
            return uploadedFileName;

        } catch (IOException e) {
            log.error("Error during file processing or upload to Google Cloud Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing file for Google Cloud Storage upload", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while uploading file to Google Cloud Storage: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred during Google Cloud Storage upload", e);
        } finally {
            // Ensure the temporary file is deleted
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                    log.debug("Temporary file deleted: {}", tempFile.getAbsolutePath());
                } catch (IOException e) {
                    log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath(), e);
                }
            }
        }
    }

    @Override
    public String getProviderName() {
        return Constant.STORAGE_GCP;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            BlobId blobId = BlobId.of(bucketName, fileName);
            return storage.readAllBytes(blobId);
        } catch (Exception e) {
            log.error("Error while downloading file {} from Google Cloud Storage: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Error while downloading file from Google Cloud Storage", e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            BlobId blobId = BlobId.of(bucketName, fileName);
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                log.info("File {} deleted from Google Cloud Storage bucket {}", fileName, bucketName);
                return fileName + " removed ...";
            } else {
                log.warn("File {} not found in Google Cloud Storage bucket {} for deletion.", fileName, bucketName);
                return fileName + " not found or could not be deleted.";
            }
        } catch (Exception e) {
            log.error("Error while deleting file {} from Google Cloud Storage: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Error while deleting file from Google Cloud Storage", e);
        }
    }
}