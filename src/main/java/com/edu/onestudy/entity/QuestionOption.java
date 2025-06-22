package com.edu.onestudy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {

    private UUID id;

    private Boolean isCorrect;

    private String text;

}

