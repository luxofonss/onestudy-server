package com.edu.onestudy.controller.pub;

import com.edu.onestudy.dto.BaseResponse;
import com.edu.onestudy.dto.pronunciation.GetSampleRequestDto;
import com.edu.onestudy.dto.pronunciation.PronunciationAccuracyRequestDto;
import com.edu.onestudy.service.BaseService;
import com.edu.onestudy.service.PronunciationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pub/pronunciations")
public class PronunciationController {

    private final BaseService baseService;

    private final PronunciationService pronunciationService;

    public PronunciationController(BaseService baseService, PronunciationService pronunciationService) {
        this.baseService = baseService;
        this.pronunciationService = pronunciationService;
    }

    @GetMapping("/samples")
    public BaseResponse<Object> getSamplePronunciations(GetSampleRequestDto request) {
        return baseService.ofSucceeded(pronunciationService.getSample(request));
    }

    @PostMapping("/accuracy")
    public BaseResponse<Object> getAccuracy(@RequestBody PronunciationAccuracyRequestDto request) {
        return baseService.ofSucceeded(pronunciationService.getAccuracy(request));
    }

}
