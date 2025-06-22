package com.edu.onestudy.dto.quiz;

import lombok.Data;

import java.util.UUID;

@Data
public class OptionDto {

    private UUID id;

    private String text;

    private Boolean isCorrect;

}
