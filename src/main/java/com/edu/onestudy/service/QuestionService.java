package com.edu.onestudy.service;

import com.edu.onestudy.dto.BaseCreateUpdateResponse;
import com.edu.onestudy.dto.quiz.QuestionDto;
import com.edu.onestudy.entity.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionService {

    BaseCreateUpdateResponse createQuestion(QuestionDto request, UUID authorId);

    BaseCreateUpdateResponse updateQuestion(QuestionDto request);

    List<Question> getAllQuestions();

    List<Question> getMyQuestions(UUID id);

    Question getById(String id);

    void deleteQuestion(String id);
}
