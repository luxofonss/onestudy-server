package com.edu.onestudy.controller.pub;

import com.edu.onestudy.annotations.CurrentUser;
import com.edu.onestudy.annotations.LogsActivityAnnotation;
import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.entity.Quiz;
import com.edu.onestudy.security.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pub/quizzes")
public class QuizPubController {

    private final QuizService quizService;

    public QuizPubController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    @LogsActivityAnnotation
    BaseResponse<List<Quiz>> getAllQuizzes(@CurrentUser UserPrincipal currentUser) {
        return BaseResponse.ofSucceeded(quizService.getAllPublicQuizzes());
    }

}
