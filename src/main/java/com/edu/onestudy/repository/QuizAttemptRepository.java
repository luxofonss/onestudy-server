package com.edu.onestudy.repository;

import com.edu.onestudy.entity.QuizAttempt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizAttemptRepository {

    QuizAttempt save(QuizAttempt attempt);

    Optional<QuizAttempt> findById(String id);

    List<QuizAttempt> findAll();

    List<QuizAttempt> findByUserId(UUID id);

    List<QuizAttempt> findByQuizId(String id);
}
