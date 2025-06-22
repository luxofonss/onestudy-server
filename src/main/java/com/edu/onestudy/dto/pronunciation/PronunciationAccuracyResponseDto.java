package com.edu.onestudy.dto.pronunciation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PronunciationAccuracyResponseDto {

    private String startTime;

    private String endTime;

    private String ipaScript;

    private String isLetterCorrectAllWords;

    private String matchedTranscripts;

    private String matchedTranscriptsIpa;

    private String pairAccuracyCategory;

    private Integer pronunciationAccuracy;

    private String realTranscript;

    private String realTranscripts;

    private String realTranscriptsIpa;

}
