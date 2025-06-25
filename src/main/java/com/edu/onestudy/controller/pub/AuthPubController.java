package com.edu.onestudy.controller.pub;

import com.edu.onestudy.annotations.LogsActivityAnnotation;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.dto.auth.AuthLoginRequest;
import com.edu.onestudy.dto.auth.AuthLoginResponse;
import com.edu.onestudy.dto.auth.AuthRegisterRequest;
import com.edu.onestudy.dto.auth.AuthRegisterResponse;
import com.edu.onestudy.service.BaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/pub/auth")
@Slf4j
public class AuthPubController {

    private final BaseService baseService;

    private final AuthService authService;

    public AuthPubController(BaseService baseService, AuthService authService) {
        this.baseService = baseService;
        this.authService = authService;
    }

    @PostMapping("/login")
    @LogsActivityAnnotation
    public BaseResponse<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return baseService.ofSucceeded(authService.login(request));
    }

    @PostMapping("/register")
    public BaseResponse<AuthRegisterResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        return baseService.ofSucceeded(authService.register(request));
    }

}
