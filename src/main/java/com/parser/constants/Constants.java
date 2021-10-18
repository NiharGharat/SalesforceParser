package com.parser.constants;

public class Constants {

    private Constants() {
    }

    // Validation - Not used outside the package
    public static final String verifyValidBaseUrlForm = "https://MyDomainName.my.salesforce.com";
    // If anytime user wants to know the format
    public static String utilUriBasicForm = "https://MyDomainName.my.salesforce.com/services/data/vXX.X/resource/";

    // Values used (default or otherwise) smwhere
    public static final String DEFAULT_VALUE_GRANT_TYPE = "password";
    public static final String DEFAULT_VALUE_LOGIN_URL = "https://login.salesforce.com";
    public static final String DEFAULT_VALUE_VERSION = "v52.0";

    // Keys expected from outside
    public static String keyBaseUrl = "baseUrl";
    public static String keyPassword = "password";
    public static String keyTableName = "tableName";
    public static String keyUsername = "userName";
    public static String keyApiTypeRequest = "apiTypeRequest";

    // Keys from
    public static final String keyAccessToken = "access_token";
    public static final String keyInstanceUrl = "instance_url";

    // Test
    public static final String envClientSecret = "SF_CLIENT_SECRET";
    public static final String envClientId = "SF_CLIENT_ID";
    public static final String envUserName = "SF_USERNAME";
    public static final String envPwd = "SF_PWD";
    public static final String envBaseUrl = "SF_BASE_URL";

    // Response
    public static final String RESPONSE_KEY_RECORDS = "records";
    public static final String RESPONSE_KEY_DONE = "done";
    public static final String RESPONSE_KEY_NEXT_URL = "nextRecordsUrl";
}
