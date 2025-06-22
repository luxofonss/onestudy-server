package com.edu.onestudy.thirdparty.pronunciation_svc;

import com.edu.onestudy.thirdparty.pronunciation_svc.request.GetSampleRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.request.PronunciationAccuracyRequest;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.GetSampleResponse;
import com.edu.onestudy.thirdparty.pronunciation_svc.response.PronunciationAccuracyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "pronunciation-service",
        url = "${thirdparty.pronunciation.service.url}"
)
public interface PronunciationFeignClient {

    @PostMapping(value = "/GetAccuracyFromRecordedAudio", produces = "application/json")
    PronunciationAccuracyResponse getAccuracyFromRecordedAudio(@RequestBody PronunciationAccuracyRequest request);

    @PostMapping(value = "/getSample", produces = "application/json")
    GetSampleResponse getSample(@RequestBody GetSampleRequest request);

}
