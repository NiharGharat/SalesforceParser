package com.parser.bean.internal;

import com.parser.com.parser.intf.InternalResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseToken implements InternalResponse {

    private String accessToken;
    private String instanceUrl;
    private String id;
    private String tokenType;
    private String issuedAt;
    private String signature;
}