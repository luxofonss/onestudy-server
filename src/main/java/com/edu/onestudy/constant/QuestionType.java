package com.edu.onestudy.constant;

import lombok.Getter;

@Getter
public enum QuestionType {
    MULTIPLE_CHOICE("multiple-choice"),
    PRONUNCIATION("pronunciation"),
    FILL_IN_THE_BLANK("fill-in-the-blank"),
    TRUE_FALSE("true-false"),
    LISTENING("listening");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }
}
