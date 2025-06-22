package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaQuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

    List<QuizAttempt> findByUserId(UUID id);

    List<QuizAttempt> findByQuizId(UUID quizId);
}
