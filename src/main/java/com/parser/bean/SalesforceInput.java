package com.parser.bean;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SalesforceInput {

    private String tableName;
    private String pwdSecurityToken;
    private String userName;
    private ApiTypes apiTypeRequested;
    // Optional
    private String baseUrl;
    // Aka consumer-key
    private String clientId;
    // Aka consumer-secret
    private String clientSecret;
    // Specific or default is v52.0
    private String version;
    // Soql query
    private String query; // Optional
    // __c thing
    private String tableApiName; // Optional
}