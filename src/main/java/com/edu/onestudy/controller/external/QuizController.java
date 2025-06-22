package com.edu.onestudy.controller.external;

import com.edu.onestudy.annotations.CurrentUser;
import com.edu.onestudy.annotations.LogsActivityAnnotation;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.dto.quiz.CreateQuizDto;
import com.edu.onestudy.dto.quiz.SubmitAnswerRequest;
import com.edu.onestudy.entity.Quiz;
import com.edu.onestudy.entity.QuizAttempt;
import com.edu.onestudy.entity.UserSavedQuiz;
import com.edu.onestudy.security.UserPrincipal;
import com.edu.onestudy.service.BaseService;
import com.edu.onestudy.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    private final BaseService baseService;

    public QuizController(QuizService quizService, BaseService baseService) {
        this.quizService = quizService;
        this.baseService = baseService;
    }

    @PostMapping
    @LogsActivityAnnotation
    BaseResponse<?> createQuiz(@RequestBody CreateQuizDto request, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.createQuiz(request, currentUser.getId()));
    }

    @PutMapping
    @LogsActivityAnnotation
    BaseResponse<?> updateQuiz(@RequestBody CreateQuizDto request, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.updateQuiz(request));
    }

    @GetMapping("/{id}")
    @LogsActivityAnnotation
    BaseResponse<Quiz> getQuizById(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getById(id));
    }

    @GetMapping("/{id}/stats")
    @LogsActivityAnnotation
    BaseResponse<Quiz> getQuizStatsById(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getQuizStatsById(id));
    }

    @GetMapping("/my")
    @LogsActivityAnnotation
    BaseResponse<List<Quiz>> getMyQuizzes(@CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getMyQuizzes(currentUser.getId()));
    }

    @DeleteMapping("/{id}")
    @LogsActivityAnnotation
    BaseResponse<?> deleteQuiz(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        quizService.deleteQuiz(id);
        return baseService.ofSucceeded(null);
    }

    @PostMapping("/{id}/save")
    @LogsActivityAnnotation
    BaseResponse<?> saveQuiz(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        quizService.saveQuiz(id, currentUser.getId());
        return baseService.ofSucceeded(null);
    }

    @DeleteMapping("/{id}/save")
    @LogsActivityAnnotation
    BaseResponse<?> unsaveQuiz(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        quizService.unsaveQuiz(id, currentUser.getId());
        return baseService.ofSucceeded(null);
    }

    @GetMapping("/{id}/save")
    @LogsActivityAnnotation
    BaseResponse<List<UserSavedQuiz>> getListUserSavedQuiz(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getUserSavedQuiz(id, currentUser.getId()));
    }

    @GetMapping("/save")
    @LogsActivityAnnotation
    BaseResponse<List<Quiz>> getSavedQuizzes(@CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getSavedQuizzes(currentUser.getId()));
    }

    @PostMapping("/{id}/attempts")
    @LogsActivityAnnotation
    BaseResponse<?> attemptQuiz(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.startQuiz(id, currentUser.getId()));
    }

    @GetMapping("/attempts")
    @LogsActivityAnnotation
    BaseResponse<List<Quiz>> getQuizAttempts(@CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getMyQuizAttempts(currentUser.getId().toString()));
    }

    @GetMapping("/attempts/{id}")
    @LogsActivityAnnotation
    BaseResponse<QuizAttempt> getQuizAttempt(@PathVariable String id, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.getAttempt(id, currentUser.getId()));
    }

    @PostMapping("/attempts/{attempt_id}/submit-question")
    @LogsActivityAnnotation
    BaseResponse<?> submitQuizQuestion(@PathVariable String attempt_id, @RequestBody SubmitAnswerRequest request, @CurrentUser UserPrincipal currentUser) {
        return baseService.ofSucceeded(quizService.submitQuizQuestion(attempt_id, request, currentUser.getId()));
    }

    @PostMapping("/attempts/{attempt_id}/complete")
    @LogsActivityAnnotation
    BaseResponse<?> submitQuizComplete(@PathVariable String attempt_id, @CurrentUser UserPrincipal currentUser) {
        quizService.submitQuizComplete(attempt_id, currentUser.getId());
        return baseService.ofSucceeded(null);
    }
}
