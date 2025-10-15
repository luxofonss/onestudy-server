package com.edu.onestudy.service;

import com.edu.onestudy.constant.ErrorConstant;
import com.edu.onestudy.entity.User;
import com.edu.onestudy.exception.BusinessException;
import com.edu.onestudy.repository.UserRepository;
import com.edu.onestudy.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsernameForAuth(username).orElseThrow(() ->
                                                                              new BusinessException(ErrorConstant.UNAUTHORIZED));

        return UserPrincipal.from(user);
    }
}
