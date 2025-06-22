package com.edu.onestudy.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@Slf4j
public class FileUtils {

    public static String convertToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception e) {
            log.warn("failed to convert file to Base64: {}", e.getMessage());
            return null;
        }
    }
}
