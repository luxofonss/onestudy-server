package com.edu.onestudy.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartQuizResponse {

    private String quizAttemptId;

    private String quizId;

    private String userId;

    private LocalDateTime startedAt;

}
