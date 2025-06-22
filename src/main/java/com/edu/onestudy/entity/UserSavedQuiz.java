package com.edu.onestudy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_saved_quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserSavedQuizId.class)
public class UserSavedQuiz {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "quiz_id", nullable = false)
    private UUID quizId;

    @Transient
    private User user;
    @Transient
    private Quiz quiz;

    @CreationTimestamp
    @Column(name = "saved_at", updatable = false)
    private LocalDateTime savedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}