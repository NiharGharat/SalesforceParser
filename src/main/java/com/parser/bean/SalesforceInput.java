package com.parser.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SalesforceInput {

    private String tableName;
    private String pwd;
    private String uName;

}
