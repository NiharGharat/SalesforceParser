package com.parser.constants;

public class Constants {
    public static final String DEFAULT_LOGIN_URL = "https://login.salesforce.com";
    public static String keyBaseUrl = "baseUrl";
    public static String keyPassword = "password";
    public static String keyTableName = "tableName";
    public static String keyUsername = "userName";

    private Constants() {
    }

    public static String keyApiTypeRequest = "apiTypeRequest";

    // If anytime user wants to know the format
    public static String utilUriBasicForm = "https://MyDomainName.my.salesforce.com/services/data/vXX.X/resource/";
}
