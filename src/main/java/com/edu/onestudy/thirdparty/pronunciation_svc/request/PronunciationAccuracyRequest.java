package com.edu.onestudy.thirdparty.pronunciation_svc.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PronunciationAccuracyRequest {

    private String base64Audio;

    private String language;

    @JsonProperty("title")
    private String text;

}
