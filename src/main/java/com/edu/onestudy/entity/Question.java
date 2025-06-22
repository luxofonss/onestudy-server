package com.edu.onestudy.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "quiz_id")
    private UUID quizId;

    @Column(name = "type")
    private String type;

    @Column(name = "text")
    private String text;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "options", columnDefinition = "jsonb")
    private List<QuestionOption> options;

    @Column(name = "pronunciation_text")
    private String pronunciationText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fill_in_blanks", columnDefinition = "jsonb")
    private List<String> correctBlanks;

    @Column(name = "true_false_answer")
    private Boolean trueFalseAnswer;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "max_listening_time")
    private Integer maxListeningTime;

    @Column(name = "correct_answer", columnDefinition = "text[]")
    @Type(ListArrayType.class)
    private List<String> correctAnswer = new ArrayList<>();

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "points")
    private Integer points;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "category")
    private String category;

    @Column(name = "accept_rate")
    private Integer acceptRate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Transient
    private Quiz quiz;

}