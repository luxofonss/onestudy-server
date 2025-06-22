package com.edu.onestudy.dto.pronunciation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPronunciationSampleResponseDto {

    private String ipaTranscript;

    private String realTranscript;

    private String transcriptTranslation;

}
