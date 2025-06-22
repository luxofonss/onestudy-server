package com.edu.onestudy.constant;

import lombok.Getter;

@Getter
public enum QuizNavigationMode {
    SEQUENTIAL("sequential"),
    BACK_ONLY("back-only"),
    FREE_NAVIGATION("free-navigation");

    private final String value;

    QuizNavigationMode(String value) {
        this.value = value;
    }
}