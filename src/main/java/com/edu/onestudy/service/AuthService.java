package com.edu.onestudy.service;

import com.edu.onestudy.dto.auth.*;

public interface AuthService {

    AuthLoginResponse login(AuthLoginRequest request);

    AuthRegisterResponse register(AuthRegisterRequest request);

    AuthProfileResponse getAuthProfile(String email);

}
