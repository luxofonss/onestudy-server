package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.UserSavedQuiz;
import com.edu.onestudy.repository.SavedQuizRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class SavedQuizRepositoryImpl implements SavedQuizRepository {

    private final JpaSavedQuizRepository jpaSavedQuizRepository;

    public SavedQuizRepositoryImpl(JpaSavedQuizRepository jpaSavedQuizRepository) {
        this.jpaSavedQuizRepository = jpaSavedQuizRepository;
    }

    @Override
    public void saveQuiz(String quizId, String userId) {
        UserSavedQuiz savedQuiz = new UserSavedQuiz();
        savedQuiz.setSavedAt(LocalDateTime.now());
        savedQuiz.setQuizId(UUID.fromString(quizId));
        savedQuiz.setUserId(UUID.fromString(userId));
        jpaSavedQuizRepository.save(savedQuiz);
    }

    @Override
    public List<UserSavedQuiz> findAllByUserId(String userId) {
        return jpaSavedQuizRepository.findByUserId(UUID.fromString(userId));
    }

    @Override
    public void deleteByQuizIdAndUserId(String quizId, String userId) {
        jpaSavedQuizRepository.deleteByQuizIdAndUserId(UUID.fromString(quizId), UUID.fromString(userId));
    }

    @Override
    public boolean existsByQuizIdAndUserId(String quizId, String userId) {
        return jpaSavedQuizRepository.existsByQuizIdAndUserId(UUID.fromString(quizId), UUID.fromString(userId));
    }

    @Override
    public List<UserSavedQuiz> findAllByQuizId(String quizId) {
        return jpaSavedQuizRepository.findAllByQuizId(UUID.fromString(quizId));
    }

    @Override
    public void update(UserSavedQuiz savedQuiz) {
        jpaSavedQuizRepository.save(savedQuiz);
    }
}
