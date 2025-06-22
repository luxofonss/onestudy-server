package com.edu.onestudy.thirdparty.pronunciation_svc.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSampleResponse {

    @JsonProperty("ipa_transcript")
    private String ipaTranscript;

    @JsonProperty("real_transcript")
    private String realTranscript;

    @JsonProperty("transcript_translation")
    private String transcriptTranslation;

}
