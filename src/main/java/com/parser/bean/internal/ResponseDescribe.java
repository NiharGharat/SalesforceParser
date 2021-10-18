package com.parser.bean.internal;

import com.parser.com.parser.intf.InternalResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDescribe implements InternalResponse {

    private String custom;
    private String label;
    private String name;
}
