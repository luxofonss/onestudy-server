package com.edu.onestudy.thirdparty.pronunciation_svc.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class GetSampleRequest {

    private String category;

    private String language;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String transcript;

}
