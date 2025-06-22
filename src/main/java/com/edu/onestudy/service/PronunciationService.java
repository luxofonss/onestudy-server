package com.edu.onestudy.service;

import com.edu.onestudy.dto.pronunciation.GetPronunciationSampleResponseDto;
import com.edu.onestudy.dto.pronunciation.GetSampleRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyResponseDto;

public interface PronunciationService {

    GetPronunciationSampleResponseDto getSample(GetSampleRequestDto request);

    PronunciationAccuracyResponseDto getAccuracy(PronunciationAccuracyRequestDto request);

}
