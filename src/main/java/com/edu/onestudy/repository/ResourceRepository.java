package com.edu.onestudy.repository;

import com.edu.onestudy.entity.Resource;

public interface ResourceRepository {

    Resource save(Resource resource);

    Resource findById(String id);

}

