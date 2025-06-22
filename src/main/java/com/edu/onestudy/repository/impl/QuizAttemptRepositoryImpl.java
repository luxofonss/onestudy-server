package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.QuizAttempt;
import com.edu.onestudy.repository.QuizAttemptRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QuizAttemptRepositoryImpl implements QuizAttemptRepository {

    private final JpaQuizAttemptRepository jpaQuizAttemptRepository;

    public QuizAttemptRepositoryImpl(JpaQuizAttemptRepository jpaQuizAttemptRepository) {
        this.jpaQuizAttemptRepository = jpaQuizAttemptRepository;
    }

    @Override
    public QuizAttempt save(QuizAttempt attempt) {
        return jpaQuizAttemptRepository.save(attempt);
    }

    @Override
    public Optional<QuizAttempt> findById(String id) {
        return jpaQuizAttemptRepository.findById(UUID.fromString(id));
    }

    @Override
    public List<QuizAttempt> findAll() {
        return jpaQuizAttemptRepository.findAll();
    }

    @Override
    public List<QuizAttempt> findByUserId(UUID id) {
        return jpaQuizAttemptRepository.findByUserId(id);
    }

    @Override
    public List<QuizAttempt> findByQuizId(String id) {
        return jpaQuizAttemptRepository.findByQuizId(UUID.fromString(id));
    }
}
