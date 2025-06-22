package com.edu.onestudy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leaderboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LeaderboardEntryId.class)
public class LeaderboardEntry {

    @Id
    @Column(name = "quiz_id", nullable = false)
    private UUID quizId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "completion_time")
    private Integer completionTime;

    @Column(name = "accuracy", precision = 3, scale = 2)
    private BigDecimal accuracy;

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "last_attempt")
    private LocalDateTime lastAttempt;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Transient
    private Quiz quiz;

    @Transient
    private User user;

}