package com.edu.onestudy.thirdparty.pronunciation_svc;

import com.edu.onestudy.constant.ErrorConstant;
import com.edu.onestudy.dto.pronunciation.GetPronunciationSampleResponseDto;
import com.edu.onestudy.dto.pronunciation.GetSampleRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyResponseDto;
import com.edu.onestudy.exception.BusinessException;
import com.edu.onestudy.mapper.PronunciationMapper;
import com.edu.onestudy.service.PronunciationService;
import com.edu.onestudy.thirdparty.pronunciation_svc.request.GetSampleRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.request.PronunciationAccuracyRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.GetSampleResponse;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.PronunciationAccuracyResponse;
import org.springframework.stereotype.Service;

@Service
public class PronunciationServiceImpl implements PronunciationService {

    private final PronunciationFeignClient pronunciationFeignClient;

    private final PronunciationMapper pronunciationMapper;

    public PronunciationServiceImpl(PronunciationFeignClient pronunciationFeignClient, PronunciationMapper pronunciationMapper) {
        this.pronunciationFeignClient = pronunciationFeignClient;
        this.pronunciationMapper = pronunciationMapper;
    }

    @Override
    public GetPronunciationSampleResponseDto getSample(GetSampleRequestDto request) {
        GetSampleRequest req = pronunciationMapper.toSampleRequest(request);
        req.setCategory(request.getLevel().getValue());
        req.setLanguage("en");
        GetSampleResponse response = pronunciationFeignClient.getSample(req);

        if (response == null) {
            throw new BusinessException(ErrorConstant.GET_PRONUNCIATION_SAMPLE_ERROR);
        }

        return pronunciationMapper.toGetPronunciationSampleResponseDto(response);
    }

    @Override
    public PronunciationAccuracyResponseDto getAccuracy(PronunciationAccuracyRequestDto request) {
        PronunciationAccuracyRequest req = pronunciationMapper.toPronunciationAccuracyRequest(request);
        req.setLanguage("en");
        PronunciationAccuracyResponse response = pronunciationFeignClient.getAccuracyFromRecordedAudio(req);

        if (response == null) {
            throw new BusinessException(ErrorConstant.GET_PRONUNCIATION_ACCURACY_ERROR);
        }

        return pronunciationMapper.toPronunciationAccuracyResponseDto(response);
    }
}
