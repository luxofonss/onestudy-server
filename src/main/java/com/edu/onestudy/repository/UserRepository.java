package com.edu.onestudy.repository;


import com.edu.onestudy.dto.user.BasicUserDto;
import com.edu.onestudy.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> getUserById(String userId);

    Optional<User> getByUsername(String username);

    Optional<User> getByUsernameForAuth(String username);

    User persist(User user);

    List<User> getByUserIdList(List<UUID> studentIds);

    List<BasicUserDto> basicSearch(String keyword);

    Optional<User> getByEmail(String username);
}
