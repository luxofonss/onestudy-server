package com.edu.onestudy.constant;

import lombok.Getter;

@Getter
public enum Difficulty {
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced");

    private final String value;

    Difficulty(String value) {
        this.value = value;
    }
}