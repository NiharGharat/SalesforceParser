package com.parser.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parser.bean.MandatoryParamException;
import com.parser.bean.ReaderOutput;
import com.parser.bean.SalesforceInput;
import com.parser.bean.internal.ResponseDescribe;
import com.parser.bean.internal.ResponseTableList;
import com.parser.constants.Constants;
import com.parser.util.CommonUtil;
import com.parser.util.SalesforceInputUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * A class for all custom salesforce api impls. The entire work flow will be defined here
 * Eg - version api method can be placed here, its all salesforce related calls will
 * be placed in the dao
 * The service layer will in turn call this Parser
 *
 */
public class SalesforceParser {

    private SalesforceDao salesforceDao;
    public SalesforceParser() {
        this.salesforceDao = new SalesforceDao(null);
    }

    private static Logger logger = LoggerFactory.getLogger(SalesforceParser.class);

    public ReaderOutput readDao(SalesforceInput salesforceInput) throws Exception {
        logger.info(">> readDao(salesforceInput)");
        // Map as other params can be required later
        HashMap<String, String> response = getToken(salesforceInput);
        validateToken(response);
        logger.info("Response token verified correctly, proceeding to reading");

        List<List<String>> records = readTable(response, salesforceInput);
        logger.info("All records loaded");
        //ReaderOutput readerOutput = createOutput(records);
        logger.info("Output dto created");

        logger.info("<< readDao(salesforceInput)");
        return null;
    }

    private void validateToken(HashMap response) throws MandatoryParamException {
        // Others are not used
        CommonUtil.checkParamForBlank(Constants.keyAccessToken, (String) response.get(Constants.keyAccessToken));
        CommonUtil.checkParamForBlank(Constants.keyInstanceUrl, (String) response.get(Constants.keyInstanceUrl));
    }

    /*
    A method to create the request, send to the common util method for post and parse response
     */
    private HashMap getToken(SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        logger.debug(">> getToken(SalesforceInput)");
        JsonObject jsonObject = salesforceDao.requestForAccessTokenAsJsonResponse(salesforceInput);
        HashMap mapResponse = CommonUtil.parseResponse(jsonObject);
        logger.debug("<< getToken(SalesforceInput)");
        return mapResponse;
    }

    /*
    Algo
    1. Get valid table name
    2. Query the table
    3. Create tokens
    4. Return
     */
    private List<List<String>> readTable(HashMap<String, String> tokens, SalesforceInput salesforceInput) throws Exception {
        final String version = SalesforceInputUtil.getVersion(salesforceInput);
        salesforceInput.setVersion(version);

        String tableApiName = salesforceInput.getTableApiName();
        final String tableName = salesforceInput.getTableName();

        if (StringUtils.isBlank(tableApiName)) {
            logger.info("Table name {} will be used", tableName);
            // get valid table name(SOQL correct name)
            tableApiName = getTableApiName(tokens, salesforceInput);
            logger.info("Table api name {} will be used");
            salesforceInput.setTableApiName(tableApiName);
        }
        logger.info("Table api name {} will be used", tableApiName);

        // execute request and parse tokens
        List<List<String>> result = fetchAndCreateResponse(tokens, salesforceInput);
        logger.info("Result created size is {}", result.size());
        return result;
    }

    private List<List<String>> fetchAndCreateResponse(HashMap<String, String> tokens, SalesforceInput salesforceInput) throws URISyntaxException, IOException, MandatoryParamException {
        logger.info(">> fetchAndCreateResponse(HashMap<String, String>, SalesforceInput)");
        String accessToken = tokens.get(Constants.keyAccessToken);
        String instanceUrl = tokens.get(Constants.keyInstanceUrl);
        StringBuilder entireQueryUrl = new StringBuilder(instanceUrl).append("/services/data/");

        // create correct query
        final String queryToUse = getQuery(tokens, salesforceInput);
        final String httpEscapedSoqlQuery = CommonUtil.escapeStringHttpUrl(queryToUse);
        entireQueryUrl.append(salesforceInput.getVersion()).append("/query?q=").append(httpEscapedSoqlQuery);

        logger.info("Url created is {}", entireQueryUrl);
        // Create response
        List<List<String>> result = new ArrayList<>();
        boolean isDone = false;
        int itr = 1;
        while (!isDone) {
            JsonObject response = salesforceDao.executeQuery(entireQueryUrl, tokens, salesforceInput);
            entireQueryUrl = parseResponse(response, result);
            isDone = StringUtils.isBlank(entireQueryUrl.toString());
            logger.debug("Iteration {} completed, completion status {}", itr++, isDone);
        }
        logger.info("<< fetchAndCreateResponse(HashMap<String, String>, SalesforceInput)");
        return result;
    }

    private StringBuilder parseResponse(JsonObject response, List<List<String>> result) throws MandatoryParamException {
        if (result == null) {
            throw new MandatoryParamException("Result object passed was with null reference");
        } else if (response == null) {
            throw new IllegalArgumentException("Result object passed was with null reference");
        }
        final JsonArray asJsonArray = response.get(Constants.RESPONSE_KEY_RECORDS).getAsJsonArray();
        boolean isFirst = result.size() == 0;
        for (JsonElement eachElem : asJsonArray) {
            List<String> eachRow = new ArrayList<>();
            List<String> headers = null;
            if (isFirst) {
                headers = new ArrayList<>();
            }
            final JsonObject eachAsJsonObject = eachElem.getAsJsonObject();
            final Set<Map.Entry<String, JsonElement>> keys = eachAsJsonObject.entrySet();
            for (Map.Entry<String, JsonElement> eachEntry : keys) {
                if (isFirst) {
                    headers.add(eachEntry.getKey());
                }
                final JsonElement value = eachEntry.getValue().getAsJsonPrimitive();
                eachRow.add(value != null ? value.getAsString() : null);
            }
            if (isFirst) {
                logger.debug("Is first parse record, headers are {}", Arrays.toString(headers.toArray()));
                result.add(headers);
                isFirst = false;
            }
            result.add(eachRow);
        }
        logger.debug("Response parsed, result size is {}", result.size());
        StringBuilder nextRecordUrl = new StringBuilder(response.get(Constants.RESPONSE_KEY_NEXT_URL) == null ? "" : response.get("nextRecordsUrl").getAsString());
        return nextRecordUrl;
    }

    private String getQuery(HashMap<String, String> tokens, SalesforceInput salesforceInput) throws URISyntaxException, IOException {
        // Get fields of the table
        List<ResponseDescribe> fields = salesforceDao.describeSobject(tokens, salesforceInput);

        // create query
        StringBuilder query = new StringBuilder("SELECT+");

        for (int i = 0; i < fields.size(); i++) {
            query.append(fields.get(i).getName());
            if (i != fields.size() - 1) {
                query.append(",");
            }
        }
        query.append("+FROM+").append(salesforceInput.getTableApiName());
        return query.toString();
    }

    private String getTableApiName(HashMap<String, String> response, SalesforceInput salesforceInput) throws Exception {
        logger.info(">> getTableApiName(HashMap<String, String>, SalesforceInput)");
        List<ResponseTableList> allTableNames =  salesforceDao.getAllTableNames(response, salesforceInput);

        final String tableName = salesforceInput.getTableName();
        String tableApiNameFound = null;
        for (ResponseTableList eachResp : allTableNames) {
            if (eachResp != null &&
                    StringUtils.isNotBlank(eachResp.getName()) &&
                    StringUtils.isNotBlank(eachResp.getLabel()) &&
                    eachResp.getName().equals(tableName + "__c")) {
                tableApiNameFound = eachResp.getName();
                break;
            }
        }
        if (tableApiNameFound == null) {
            throw new Exception(String.format("List of table names did not contain speciifed table name %s", tableName));
        } else {
            logger.info(String.format("Api name for specified table name is %s", tableApiNameFound));
        }
        logger.info("<< getTableApiName(HashMap<String, String>, SalesforceInput)");
        return tableApiNameFound;
    }
}
