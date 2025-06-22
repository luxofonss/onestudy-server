package com.edu.onestudy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quiz_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAnswer {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "quiz_attempt_id", nullable = false)
    private UUID quizAttemptId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "selected_answers", columnDefinition = "jsonb")
    private List<QuestionOption> selectedAnswers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fill_in_blanks_answers", columnDefinition = "jsonb")
    private List<String> fillInBlanksAnswers;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @Column(name = "score_achieved")
    private Integer scoreAchieved;

    @Column(name = "time_taken")
    private Integer timeTaken;

    @Column(name = "audio_url")
    private String audioUrl;

    @CreationTimestamp
    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

}
