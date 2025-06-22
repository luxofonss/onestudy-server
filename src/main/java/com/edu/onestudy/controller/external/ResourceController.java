package com.edu.onestudy.controller.external;

import com.edu.onestudy.annotations.CurrentUser;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.dto.resource.GetResourceResponse;
import com.edu.onestudy.entity.Resource;
import com.edu.onestudy.security.UserPrincipal;
import com.edu.onestudy.service.BaseService;
import com.edu.onestudy.service.ResourceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {

    private final BaseService baseService;

    private final ResourceService resourceService;

    public ResourceController(BaseService baseService, ResourceService resourceService) {
        this.baseService = baseService;
        this.resourceService = resourceService;
    }

    @PostMapping()
    public BaseResponse<Resource> uploadResource(
            @CurrentUser UserPrincipal requester,
            @RequestParam("file") MultipartFile file
    ) {
        return baseService.ofSucceeded(resourceService.uploadResource(file, requester.getId()));
    }

    @GetMapping("/{id}")
    public BaseResponse<GetResourceResponse> getResource(
            @CurrentUser UserPrincipal requester,
            @PathVariable String id
    ) {
        return baseService.ofSucceeded(resourceService.getResource(id, requester.getId()));
    }

}
