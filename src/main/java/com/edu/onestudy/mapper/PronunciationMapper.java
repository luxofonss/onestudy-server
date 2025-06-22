package com.edu.onestudy.mapper;

import com.edu.onestudy.dto.pronunciation.GetPronunciationSampleResponseDto;
import com.edu.onestudy.dto.pronunciation.GetSampleRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyResponseDto;
import com.edu.onestudy.thirdparty.pronunciation_svc.request.GetSampleRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.request.PronunciationAccuracyRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.GetSampleResponse;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.PronunciationAccuracyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PronunciationMapper {

    @Mapping(target = "transcript", source = "customText")
    GetSampleRequest toSampleRequest(GetSampleRequestDto req);

    GetPronunciationSampleResponseDto toGetPronunciationSampleResponseDto(GetSampleResponse response);

    PronunciationAccuracyRequest toPronunciationAccuracyRequest(PronunciationAccuracyRequestDto req);

    PronunciationAccuracyResponseDto toPronunciationAccuracyResponseDto(PronunciationAccuracyResponse response);

}
