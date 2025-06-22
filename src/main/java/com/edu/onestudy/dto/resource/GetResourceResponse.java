package com.edu.onestudy.dto.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetResourceResponse {

    private String url;

    private String name;

}
