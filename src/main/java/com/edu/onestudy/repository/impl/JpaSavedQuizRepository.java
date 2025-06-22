package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.UserSavedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaSavedQuizRepository extends JpaRepository<UserSavedQuiz,  UUID> {
    List<UserSavedQuiz> findByUserId(UUID userId);

    void deleteByQuizIdAndUserId(UUID quizId, UUID userId);

    boolean existsByQuizIdAndUserId(UUID quizId, UUID userId);

    List<UserSavedQuiz> findAllByQuizId(UUID quizId);
}