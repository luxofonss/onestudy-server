package com.edu.onestudy.repository;

import com.edu.onestudy.entity.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface QuizRepository {

    void save(Quiz quiz);

    Optional<Quiz> findById(String id);

    List<Quiz> findAll();

    List<Quiz> findByUserId(UUID id);

    List<Quiz> findByIdIn(Set<String> quizId);

    List<Quiz> findAllPublic();

}
