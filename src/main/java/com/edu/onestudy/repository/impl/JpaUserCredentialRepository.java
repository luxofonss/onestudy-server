package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaUserCredentialRepository extends JpaRepository<UserCredential, UUID> {

    UserCredential findByUserId(UUID userId);

}