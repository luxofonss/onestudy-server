package com.edu.onestudy.service;

import com.edu.onestudy.dto.resource.GetResourceResponse;
import com.edu.onestudy.entity.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public interface ResourceService {

    Resource uploadResource(MultipartFile file, UUID requesterId);

    GetResourceResponse getResource(String id, UUID requesterId);

    File downloadResourceFromUrl(String url);

}
