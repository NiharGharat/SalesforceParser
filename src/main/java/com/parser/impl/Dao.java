package com.parser.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parser.bean.ReaderOutput;
import com.parser.bean.ResponseToken;
import com.parser.bean.SalesforceInput;
import com.parser.constants.Constants;
import com.parser.util.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/*
1. REST username-pwd flow
2. BULK api
3. REST oAuth jwt path
*/
public class Dao {

    private static Logger logger = LoggerFactory.getLogger(Dao.class);

    public ReaderOutput readDao(SalesforceInput salesforceInput) throws Exception {
        logger.info(">> readDao(salesforceInput)");
        ResponseToken response = getToken(salesforceInput);
        validateToken(response);

        logger.info("<< readDao(salesforceInput)");
        return null;
    }

    /*
    A method to create the request, send to the common util method for post and parse response
     */
    private ResponseToken getToken(SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        logger.debug(">> getToken(SalesforceInput)");

        HttpResponse response = getTokenHttpResponse(salesforceInput);
        logger.info("Status of request is {}", response.getStatusLine().getStatusCode());

        JsonObject jsonObject = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                jsonObject = gson.fromJson(line, JsonObject.class);
            }
        }

        ResponseToken responseToken = ResponseToken.parseResponse(jsonObject);

        logger.debug("<< getToken(SalesforceInput)");
        return responseToken;
    }

    private HttpResponse getTokenHttpResponse(SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        // Base url is already valid
        StringBuilder baseUrl = new StringBuilder(salesforceInput.getBaseUrl());
        baseUrl.append("/services/oauth2/token");

        URI salesforceUri = new URI(baseUrl.toString());
        HttpPost httpPost = new HttpPost(salesforceUri);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        // form encode requires the payload in this format
        StringBuilder bodyPayload = new StringBuilder();
        bodyPayload
                .append("grant_type").append("=").append(Constants.VALUE_GRANT_TYPE)
                .append("&client_id").append("=").append(salesforceInput.getClientId())
                .append("&client_secret").append("=").append(salesforceInput.getClientSecret())
                .append("&username").append("=").append(salesforceInput.getUserName())
                .append("&password").append("=").append(salesforceInput.getPwd());

        HttpResponse response = HttpUtil.doPost(baseUrl.toString(), headers, bodyPayload.toString());
        return response;
    }
}
