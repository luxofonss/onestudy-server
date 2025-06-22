package com.edu.onestudy.dto.quiz;

import com.edu.onestudy.constant.QuizNavigationMode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateQuizDto {

    private String id;

    private String title;

    private String description;

    private List<QuestionDto> questions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer totalPoints;

    private String difficulty;

    private Integer estimatedDuration;

    private List<String> tags;

    private Boolean isPublic;

    private Integer version;

    private QuizNavigationMode navigationMode;

    private Boolean hasTimer;

    private Integer timeLimit;

    private Integer warningTime;

    private Boolean allowQuestionPicker;

    private Boolean shuffleQuestions;

    private Boolean shuffleAnswers;

    private Boolean showProgress;

    private Boolean allowPause;

    private Integer maxAttempts;

    private Integer passingScore;

}
