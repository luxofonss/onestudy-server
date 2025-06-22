package com.edu.onestudy.constant;

import lombok.Getter;

@Getter
public enum PronunciationLevel {
    HARD("3"),
    MEDIUM("2"),
    EASY("1"),
    RANDOM("0"),
    ;

    private final String value;

    PronunciationLevel(String value) {
        this.value = value;
    }

    public static PronunciationLevel fromLevel(String level) {
        for (PronunciationLevel pronunciationLevel : values()) {
            if (pronunciationLevel.getValue().equals(level)) {
                return pronunciationLevel;
            }
        }
        throw new IllegalArgumentException("Unknown level: " + level);
    }

}
