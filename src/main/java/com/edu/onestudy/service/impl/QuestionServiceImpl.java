package com.edu.onestudy.service.impl;

import com.edu.onestudy.dto.BaseCreateUpdateResponse;
import com.edu.onestudy.dto.quiz.CreateQuizDto;
import com.edu.onestudy.dto.quiz.QuestionDto;
import com.edu.onestudy.entity.Question;
import com.edu.onestudy.repository.QuestionRepository;
import com.edu.onestudy.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public BaseCreateUpdateResponse createQuestion(QuestionDto request, UUID authorId) {
        return null;
    }

    @Override
    public BaseCreateUpdateResponse updateQuestion(QuestionDto request) {
        return null;
    }

    @Override
    public List<Question> getAllQuestions() {
        return List.of();
    }

    @Override
    public List<Question> getMyQuestions(UUID id) {
        return List.of();
    }

    @Override
    public Question getById(String id) {
        return null;
    }

    @Override
    public void deleteQuestion(String id) {

    }
}
