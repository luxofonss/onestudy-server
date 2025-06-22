package com.edu.onestudy.thirdparty.storage;

import com.edu.onestudy.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component("awsStorageService")
@Slf4j
public class AwsStorageServiceImpl extends StorageService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.bucket.domain}")
    private String domain;

    public AwsStorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String getFileUrl(String fileName) {
        return String.format("%s/%s", domain, fileName);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        File fileObj = null;
        try {
            fileObj = convertMultiPartFileToFile(file);
            String fileName = Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "") + "_" + System.currentTimeMillis() + "."
                    + Objects.requireNonNull(file.getContentType()).split("/")[1];

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(fileObj));

            return getFileUrl(fileName);
        } catch (IOException e) {
            log.error("Error during file processing (conversion or deletion): {}", e.getMessage(), e);
            throw new RuntimeException("Error processing file for S3 upload", e);
        }
        catch (Exception e) {
            log.error("Error while uploading file to S3: {}", e.getMessage(), e);
            throw new RuntimeException("Error while uploading file to S3", e);
        } finally {
            if (fileObj != null && fileObj.exists()) {
                if (!fileObj.delete()) {
                    log.warn("Failed to delete temporary file: {}", fileObj.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public String getProviderName() {
        return Constant.STORAGE_AWS;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            // Build the GetObjectRequest for S3 download
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Get the object content as a ResponseInputStream and read all bytes
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return s3Object.readAllBytes();
        } catch (IOException e) {
            log.error("Error reading downloaded file content: {}", e.getMessage(), e);
            throw new RuntimeException("Error while reading downloaded file content", e);
        } catch (Exception e) {
            log.error("Error while downloading file from S3: {}", e.getMessage(), e);
            throw new RuntimeException("Error while downloading file from S3", e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        try {
            // Build the DeleteObjectRequest for S3 deletion
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return fileName + " removed ...";
        } catch (Exception e) {
            log.error("Error while deleting file from S3: {}", e.getMessage(), e);
            throw new RuntimeException("Error while deleting file from S3", e);
        }
    }
}
