package com.edu.onestudy.controller.external;

import com.edu.onestudy.annotations.CurrentUser;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.dto.auth.AuthProfileResponse;
import com.edu.onestudy.security.UserPrincipal;
import com.edu.onestudy.service.AuthService;
import com.edu.onestudy.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final BaseService baseService;

    private final AuthService authService;

    public AuthController(BaseService baseService, AuthService authService) {
        this.baseService = baseService;
        this.authService = authService;
    }

    @GetMapping("/me")
    public BaseResponse<AuthProfileResponse> whoAmI(@CurrentUser UserPrincipal requester) {
        return baseService.ofSucceeded(authService.getAuthProfile(requester.getId().toString()));
    }
}
