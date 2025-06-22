package com.edu.onestudy.repository.impl;

import com.edu.onestudy.constant.QuizStatus;
import com.edu.onestudy.entity.Quiz;
import com.edu.onestudy.repository.QuizRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class QuizRepositoryImpl implements QuizRepository {

    private final JpaQuizRepository repository;

    public QuizRepositoryImpl(JpaQuizRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Quiz quiz) {
        repository.save(quiz);
    }

    @Override
    public Optional<Quiz> findById(String id) {
        return repository.findById(UUID.fromString(id));
    }

    @Override
    public List<Quiz> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Quiz> findByUserId(UUID id) {
        return repository.findByAuthorId(id);
    }

    @Override
    public List<Quiz> findByIdIn(Set<String> quizId) {
        return repository.findByIdIn(quizId.stream()
                .map(UUID::fromString)
                .toList());
    }

    @Override
    public List<Quiz> findAllPublic() {
        return repository.findByStatus(QuizStatus.PUBLIC);
    }
}
