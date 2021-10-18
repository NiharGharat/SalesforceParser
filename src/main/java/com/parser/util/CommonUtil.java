package com.parser.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parser.bean.MandatoryParamException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CommonUtil {

    private CommonUtil() {

    }

    public static void checkParamForBlank(String paramName, String paramValue) throws MandatoryParamException {
        if (StringUtils.isBlank(paramValue)) {
            String errorMsg = String.format("Mandatory parameter %s was null/Blank", paramName);
            throw new MandatoryParamException(errorMsg);
        }
    }

    public static HashMap parseResponse(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject, HashMap.class);
    }

    public static String escapeStringHttpUrl(String query) throws UnsupportedEncodingException {
        return URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
    }

    public static JsonObject httpResponseToJson(HttpResponse response) throws IOException {
        JsonObject jsonObject = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                jsonObject = gson.fromJson(line, JsonObject.class);
            }
        }
        return jsonObject;
    }
}
