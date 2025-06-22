package com.edu.onestudy.service;

import com.edu.onestudy.dto.BaseCreateUpdateResponse;
import com.edu.onestudy.dto.quiz.CreateQuizDto;
import com.edu.onestudy.dto.quiz.SubmitAnswerRequest;
import com.edu.onestudy.entity.Quiz;
import com.edu.onestudy.entity.QuizAttempt;
import com.edu.onestudy.entity.UserSavedQuiz;

import java.util.List;
import java.util.UUID;

public interface QuizService {

    BaseCreateUpdateResponse createQuiz(CreateQuizDto request, UUID authorId);

    BaseCreateUpdateResponse updateQuiz(CreateQuizDto request);

    List<Quiz> getAllPublicQuizzes();

    List<Quiz> getMyQuizzes(UUID id);

    Quiz getById(String id);

    void deleteQuiz(String id);

    BaseCreateUpdateResponse startQuiz(String quizId, UUID userId);

    BaseCreateUpdateResponse submitQuizQuestion(String attemptId, SubmitAnswerRequest request, UUID id);

    List<Quiz> getMyQuizAttempts(String string);

    QuizAttempt getAttempt(String attemptId, UUID userId);

    void submitQuizComplete(String attemptId, UUID id);

    Quiz getQuizStatsById(String id);

    void saveQuiz(String quizId, UUID userId);

    List<Quiz> getSavedQuizzes(UUID userId);

    void unsaveQuiz(String quizId, UUID userId);

    List<UserSavedQuiz> getUserSavedQuiz(String quizId, UUID userId);
}
