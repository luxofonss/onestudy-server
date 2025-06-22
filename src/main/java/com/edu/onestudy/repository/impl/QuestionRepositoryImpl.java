package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.Question;
import com.edu.onestudy.repository.QuestionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final JpaQuestionRepository repository;

    public QuestionRepositoryImpl(JpaQuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Question save(Question question) {
        return repository.save(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findByQuizId(String quizId) {
        return repository.findByQuizId(UUID.fromString(quizId));
    }

    @Override
    public void saveAll(List<Question> questions) {
        repository.saveAll(questions);
    }

    @Override
    public void deleteAll(List<Question> questions) {
        repository.deleteAll(questions);
    }

    @Override
    public Integer countByQuizId(String quizId) {
        return repository.countByQuizId(UUID.fromString(quizId));
    }
}
