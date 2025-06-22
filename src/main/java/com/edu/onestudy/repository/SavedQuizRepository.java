package com.edu.onestudy.repository;

import com.edu.onestudy.entity.UserSavedQuiz;

import java.util.List;

public interface SavedQuizRepository {

    void saveQuiz(String quizId, String userId);

    List<UserSavedQuiz> findAllByUserId(String userId);

    void deleteByQuizIdAndUserId(String quizId, String userId);

    boolean existsByQuizIdAndUserId(String quizId, String userId);

    List<UserSavedQuiz> findAllByQuizId(String quizId);

    void update(UserSavedQuiz savedQuiz);
}
