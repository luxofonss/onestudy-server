package com.edu.onestudy.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedQuizId implements Serializable {
    private UUID userId;
    private UUID quizId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSavedQuizId that = (UserSavedQuizId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(quizId, that.quizId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, quizId);
    }
}