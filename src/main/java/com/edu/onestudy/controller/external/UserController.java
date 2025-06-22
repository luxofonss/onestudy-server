package com.edu.onestudy.controller.external;

import com.edu.onestudy.annotations.CurrentUser;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.security.UserPrincipal;
import com.edu.onestudy.service.BaseService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final BaseService baseService;

    public UserController(BaseService baseService) {
        this.baseService = baseService;
    }

    @PutMapping
    public BaseResponse<?> uploadResource(
            @CurrentUser UserPrincipal requester
    ) {
        
        return baseService.ofSucceeded(null);
    }

}
