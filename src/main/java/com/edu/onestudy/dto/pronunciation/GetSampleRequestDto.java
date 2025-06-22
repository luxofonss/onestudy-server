package com.edu.onestudy.dto.pronunciation;

import com.edu.onestudy.constant.PronunciationLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSampleRequestDto {

    @NotNull
    private PronunciationLevel level;

    private String customText;

}
