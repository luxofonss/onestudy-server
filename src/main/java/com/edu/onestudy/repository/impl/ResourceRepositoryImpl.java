package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.Resource;
import com.edu.onestudy.repository.ResourceRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ResourceRepositoryImpl implements ResourceRepository {

    private final JpaResourceRepository repository;

    public ResourceRepositoryImpl(JpaResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Resource save(Resource resource) {
        return repository.save(resource);
    }

    @Override
    public Resource findById(String id) {
        return repository.findById(UUID.fromString(id)).orElse(null);
    }
}
