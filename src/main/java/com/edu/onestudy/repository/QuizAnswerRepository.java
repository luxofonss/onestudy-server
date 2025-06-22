package com.edu.onestudy.repository;

import com.edu.onestudy.entity.QuizAnswer;

import java.util.List;
import java.util.Optional;

public interface QuizAnswerRepository {

    QuizAnswer save(QuizAnswer answer);

    void saveAll(List<QuizAnswer> answers);

    Optional<QuizAnswer> findById(String id);

    List<QuizAnswer> findByQuizAttemptId(String attemptId);

    void delete(QuizAnswer oldAnswer);
}
