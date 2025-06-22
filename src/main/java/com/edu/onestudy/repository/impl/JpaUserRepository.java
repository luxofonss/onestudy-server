package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    List<User> findAllByIdIn(List<UUID> userIds);

    Optional<User> findByEmail(String email);
}