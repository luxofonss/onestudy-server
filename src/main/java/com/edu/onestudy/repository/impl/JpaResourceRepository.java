package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaResourceRepository extends JpaRepository<Resource, UUID> {
}
