package com.edu.onestudy.dto.pronunciation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PronunciationAccuracyRequestDto {

    private String base64Audio;

    private String text;

}
