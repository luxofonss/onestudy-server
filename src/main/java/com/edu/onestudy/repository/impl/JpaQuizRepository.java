package com.edu.onestudy.repository.impl;

import com.edu.onestudy.constant.QuizStatus;
import com.edu.onestudy.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaQuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByAuthorId(UUID authorId);

    List<Quiz> findByIdIn(Collection<UUID> ids);

    List<Quiz> findByStatus(QuizStatus status);
}
