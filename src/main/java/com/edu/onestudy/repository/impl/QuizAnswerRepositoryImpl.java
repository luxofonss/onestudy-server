package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.QuizAnswer;
import com.edu.onestudy.repository.QuizAnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QuizAnswerRepositoryImpl implements QuizAnswerRepository {

    private final JpaQuizAnswerRepository repository;

    public QuizAnswerRepositoryImpl(JpaQuizAnswerRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuizAnswer save(QuizAnswer answer) {
        return repository.save(answer);
    }

    @Override
    public void saveAll(List<QuizAnswer> answers) {
        repository.saveAll(answers);
    }

    @Override
    public Optional<QuizAnswer> findById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public List<QuizAnswer> findByQuizAttemptId(String attemptId) {
        return repository.findByQuizAttemptId(UUID.fromString(attemptId));
    }

    @Override
    public void delete(QuizAnswer oldAnswer) {
        repository.delete(oldAnswer);
    }
}
