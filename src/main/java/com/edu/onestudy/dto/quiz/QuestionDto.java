package com.edu.onestudy.dto.quiz;

import com.edu.onestudy.constant.Difficulty;
import com.edu.onestudy.constant.QuestionType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDto {

    private String id;

    private QuestionType type;

    private String text;

    private List<OptionDto> options;

    private String pronunciationText;

    private List<String> correctBlanks;

    private Boolean trueFalseAnswer;

    private String audioUrl;

    private String imageUrl;

    private Integer maxListeningTime;

    private int points;

    private Difficulty difficulty;

    private Integer acceptRate;

    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}