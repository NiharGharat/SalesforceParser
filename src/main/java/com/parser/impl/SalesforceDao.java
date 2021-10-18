package com.parser.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.parser.bean.SalesforceInput;
import com.parser.bean.internal.ResponseDescribe;
import com.parser.bean.internal.ResponseTableList;
import com.parser.constants.Constants;
import com.parser.util.CommonUtil;
import com.parser.util.HttpUtil;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
All salesforce dao interactions methods     -   Dao
All initiating request methods(flow)        -   impl
All response parsing methods                -   parser

1. REST username-pwd flow
2. BULK api
3. REST oAuth jwt path
*/
public class SalesforceDao {

    private final Gson gsonToUse;

    public SalesforceDao(Gson injectedGson) {
        if (injectedGson == null) {
            this.gsonToUse = new Gson();
        } else {
            this.gsonToUse = injectedGson;
        }
    }

    private static Logger logger = LoggerFactory.getLogger(SalesforceDao.class);

    /*
    Request Access token and send out the responseObject to be parsed individually
     */
    private HttpResponse requestForAccessToken(SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        logger.debug(">> requestForAccessToken(SalesforceInput)");
        // form encode requires the payload in this format
        StringBuilder bodyPayload = new StringBuilder();
        bodyPayload
                .append("grant_type").append("=").append(Constants.DEFAULT_VALUE_GRANT_TYPE)
                .append("&client_id").append("=").append(salesforceInput.getClientId())
                .append("&client_secret").append("=").append(salesforceInput.getClientSecret())
                .append("&username").append("=").append(salesforceInput.getUserName())
                .append("&password").append("=").append(salesforceInput.getPwdSecurityToken());

        // Base url is already valid
        StringBuilder baseUrl = new StringBuilder(salesforceInput.getBaseUrl());
        baseUrl.append("/services/oauth2/token");
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        final HttpResponse response = HttpUtil.doPost(baseUrl.toString(), headers, bodyPayload.toString());
        logger.info("Status of request is {}", response.getStatusLine().getStatusCode());
        logger.debug("<< requestForAccessToken(SalesforceInput)");
        return response;
    }

    public JsonObject requestForAccessTokenAsJsonResponse(SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        logger.debug(">> requestForAccessTokenAsJsonResponse(SalesforceInput)");
        final HttpResponse response = requestForAccessToken(salesforceInput);
        JsonObject jsonObject = CommonUtil.httpResponseToJson(response);
        logger.debug("<< requestForAccessTokenAsJsonResponse(SalesforceInput)");
        return jsonObject;
    }

    public List<ResponseTableList> getAllTableNames(HashMap<String, String> tokens, SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        String accessToken = tokens.get(Constants.keyAccessToken);
        String instanceUrl = tokens.get(Constants.keyInstanceUrl);

        StringBuilder baseUrl = new StringBuilder(instanceUrl).append("/services/data/").append(salesforceInput.getVersion()).append("/sobjects/");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", accessToken));
        final HttpResponse response = HttpUtil.doGet(baseUrl.toString(), headers);
        //final HttpResponse response = HttpUtil.doPost(baseUrl.toString(), headers, null);
        final JsonObject jsonObject = CommonUtil.httpResponseToJson(response);

        final Type type = new TypeToken<List<ResponseTableList>>() {}.getType();
        return this.gsonToUse.fromJson(jsonObject.get("sobjects").getAsJsonArray(), type);
    }

    public List<ResponseDescribe> describeSobject(HashMap<String, String> tokens, SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        String accessToken = tokens.get(Constants.keyAccessToken);
        String instanceUrl = tokens.get(Constants.keyInstanceUrl);

        StringBuilder baseUrl = new StringBuilder(instanceUrl).append("/services/data/");
        baseUrl.append(salesforceInput.getVersion()).append("/sobjects/").append(salesforceInput.getTableApiName()).append("/describe/");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", accessToken));
        final HttpResponse response = HttpUtil.doGet(baseUrl.toString(), headers);
        final JsonObject jsonObject = CommonUtil.httpResponseToJson(response);
        //
        final Type type = new TypeToken<List<ResponseDescribe>>() {}.getType();
        return this.gsonToUse.fromJson(jsonObject.get("fields").getAsJsonArray(), type);
    }

    public JsonObject executeQuery(StringBuilder url, HashMap<String, String> tokens, SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        String accessToken = tokens.get(Constants.keyAccessToken);
        String instanceUrl = tokens.get(Constants.keyInstanceUrl);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", accessToken));
        final HttpResponse response = HttpUtil.doGet(url.toString(), headers);
        return CommonUtil.httpResponseToJson(response);
    }
}