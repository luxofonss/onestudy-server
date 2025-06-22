package com.edu.onestudy.repository.impl;

import com.edu.onestudy.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, UUID> {

     List<Question> findByQuizId(UUID quizId);

     Integer countByQuizId(UUID quizId);

}
