package com.parser.constants;

public class Constants {
    private Constants() {
    }

    // Validation
    public static final String verifyValidBaseUrlForm = "https://MyDomainName.my.salesforce.com";

    public static final String VALUE_GRANT_TYPE = "password";
    public static final String DEFAULT_LOGIN_URL = "https://login.salesforce.com";
    public static String keyBaseUrl = "baseUrl";
    public static String keyPassword = "password";
    public static String keyTableName = "tableName";

    public static String keyUsername = "userName";

    public static String keyApiTypeRequest = "apiTypeRequest";

    // If anytime user wants to know the format
    public static String utilUriBasicForm = "https://MyDomainName.my.salesforce.com/services/data/vXX.X/resource/";

    // Test
    public static final String envClientSecret = "SF_CLIENT_SECRET";
    public static final String envClientId = "SF_CLIENT_ID";
    public static final String envUserName = "SF_USERNAME";
    public static final String envPwd = "SF_PWD";
    public static final String envBaseUrl = "SF_BASE_URL";
}
