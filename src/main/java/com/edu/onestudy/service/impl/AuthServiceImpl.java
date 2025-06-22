package com.edu.onestudy.service.impl;

import com.edu.onestudy.constant.ErrorConstant;
import com.edu.onestudy.constant.UserRole;
import com.edu.onestudy.dto.auth.*;
import com.edu.onestudy.entity.User;
import com.edu.onestudy.entity.UserCredential;
import com.edu.onestudy.exception.BusinessException;
import com.edu.onestudy.mapper.UserMapper;
import com.edu.onestudy.repository.UserRepository;
import com.edu.onestudy.security.JwtProvider;
import com.edu.onestudy.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        Optional<User> user = userRepository.getByUsername(request.getUsername());
        if (user.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User userData = userMapper.authRegisterRequestToUser(request);
        userData.setRole(UserRole.USER);
        UserCredential credentials = UserCredential
                .builder()
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userId(userData.getId())
                .build();
        userData.setCredentials(credentials);
        userRepository.persist(userData);
        return new AuthRegisterResponse(userData.getId().toString());
    }

    @Override
    public AuthProfileResponse getAuthProfile(String id) {
        User user = userRepository.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.userToAuthProfileResponse(user);
    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return new AuthLoginResponse(
                    jwtProvider.generateAccessToken(authentication),
                    jwtProvider.generateRefreshToken(authentication));
        } catch (Exception ex) {
            log.error("Login failed for user {}: {}", request.getUsername(), ex.getMessage());
            throw new BusinessException(ErrorConstant.USERNAME_PASSWORD_WRONG);
        }
    }

}
