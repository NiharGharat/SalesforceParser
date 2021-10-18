package com.parser.bean.internal;

import com.parser.com.parser.intf.InternalResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTableList implements InternalResponse {

    /*
    All variables from response of describe
     */

    private String name;
    private String label;
}
