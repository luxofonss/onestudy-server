package com.edu.onestudy.repository;

import com.edu.onestudy.entity.Question;
import com.edu.onestudy.entity.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Question save(Question question);

    Optional<Question> findById(String id);

    List<Question> findByQuizId(String quizId);

    void saveAll(List<Question> questions);

    void deleteAll(List<Question> questions);

    Integer countByQuizId(String quizId);

}
