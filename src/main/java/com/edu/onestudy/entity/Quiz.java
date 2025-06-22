package com.edu.onestudy.entity;

import com.edu.onestudy.constant.QuizStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set; // Keep for convenience if you load and set them as transient
import java.util.UUID;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "question_count")
    private Integer questionCount = 0;

    @Column(name = "tags", columnDefinition = "text[]")
    @Type(ListArrayType.class)
    private List<String> tags = new ArrayList<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private QuizStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "author_id") // This is the ID field for the conceptual author
    private UUID authorId;

    @Transient
    private User author;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(0.00);

    @Column(name = "attempts")
    private Integer attempts = 0;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "navigation_mode")
    private String navigationMode;

    @Column(name = "has_timer")
    private Boolean hasTimer = false;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "warning_time")
    private Integer warningTime;

    @Column(name = "allow_question_picker")
    private Boolean allowQuestionPicker = false;

    @Column(name = "shuffle_questions")
    private Boolean shuffleQuestions = false;

    @Column(name = "shuffle_answers")
    private Boolean shuffleAnswers = false;

    @Column(name = "show_progress")
    private Boolean showProgress = false;

    @Column(name = "allow_pause")
    private Boolean allowPause = false;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Transient
    private Set<Question> questions;

    @Transient
    private Set<QuizAttempt> quizAttempts;

    @Transient
    private Set<User> savedByUsers;

    @Transient
    private Set<LeaderboardEntry> leaderboardEntries;

}