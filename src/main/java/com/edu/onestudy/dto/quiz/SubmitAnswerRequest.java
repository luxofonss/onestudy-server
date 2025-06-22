package com.edu.onestudy.dto.quiz;

import lombok.Data;

import java.util.List;

@Data
public class SubmitAnswerRequest {

    private String questionId;

    private List<String> selectedOptions;

    private List<String> fillInBlanksAnswers;

    private String answerText;

    private Boolean userAnswerTrueFalse;

    private Integer timeTaken;

    private String audioUrl;

}
