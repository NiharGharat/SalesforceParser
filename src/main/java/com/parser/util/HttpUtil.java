package com.parser.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static HttpResponse doPost(String baseUrl, Map<String, String> headers, String bodyPayload) throws URISyntaxException, IOException {
        logger.debug(">> doPost(String baseUrl, Map<String, String> headers, String bodyPayload)");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI salesforceUri = new URI(baseUrl);
        HttpPost httpPost = new HttpPost(salesforceUri);
        for (Map.Entry<String, String> eachHeader : headers.entrySet()) {
            httpPost.addHeader(eachHeader.getKey(), eachHeader.getValue());
        }
        final StringEntity entity = new StringEntity(bodyPayload);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        logger.info("Status of request is {}", response.getStatusLine().getStatusCode());

        logger.debug("<< doPost(String baseUrl, Map<String, String> headers, String bodyPayload)");
        return response;
    }

    public static HttpResponse doGet(String baseUrl, Map<String, String> headers) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI salesforceUri = new URI(baseUrl);
        HttpGet httpGet = new HttpGet(salesforceUri);
        for (Map.Entry<String, String> eachHeader : headers.entrySet()) {
            httpGet.addHeader(eachHeader.getKey(), eachHeader.getValue());
        }
        HttpResponse response = httpClient.execute(httpGet);
        logger.info("Status of request is {}", response.getStatusLine().getStatusCode());

        logger.debug("<< doPost(String baseUrl, Map<String, String> headers, String bodyPayload)");
        return response;
    }
}
