package com.edu.onestudy.thirdparty.pronunciation_svc.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class PronunciationAccuracyResponse {

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
