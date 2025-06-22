package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaQuizAnswerRepository extends JpaRepository<QuizAnswer, UUID> {
    List<QuizAnswer> findByQuizAttemptId(UUID quizAttemptId);
}
