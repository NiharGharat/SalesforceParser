package com.parser.impl;

import com.parser.bean.ApiTypes;
import com.parser.bean.MandatoryParamException;
import com.parser.bean.ReaderOutput;
import com.parser.bean.SalesforceInput;
import com.parser.com.parser.intf.Reader;
import com.parser.com.parser.intf.Writer;
import com.parser.constants.Constants;
import com.parser.util.CommonUtil;
import com.parser.util.SalesforceInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class SalesforceParserImpl implements Reader, Writer {

    private Dao dao;
    private static Logger logger = LoggerFactory.getLogger(SalesforceParserImpl.class);
    public SalesforceParserImpl() {
        this.dao = new Dao();
    }

    @Override
    public void write(ReaderOutput data) {
        return;
    }

    /**
     * A method to read using given credentials and create output in rowWise List<List<String>>
     * format
     * It also has the datatype support {@link ReaderOutput}, and different auth avbl {@link ApiTypes}
     *</br>
     * This method maps the map param key value to input class,
     * Verifies minimum required inputs
     * Logs necessary inputs
     * Reads from Dao(dao maps to readerOutput)
     * Return output
     *
     * @param param - All param flags with keys defined as {@link Constants} with key prefix
     * @return {@link ReaderOutput}
     * @throws MandatoryParamException -
     */
    @Override
    public ReaderOutput read(Map<String, Object> param) throws Exception {
        logger.debug(">> read(param)");
        // Map the params to salesforceInput class
        SalesforceInput salesforceInput = SalesforceInputUtil.createInput(param);
        // Verify min inputs required
        verifyInputs(salesforceInput);
        // Log necessary once them
        logInputs(salesforceInput);
        // Pass the input to dao and get result class
        ReaderOutput readerOutput = dao.readDao(salesforceInput);
        logger.debug("<< read(param)");
        return readerOutput;
    }

    private void logInputs(SalesforceInput salesforceInput) {
        logger.debug(">> logInputs(SalesforceInput)");
        logger.debug("BaseUrl {}", salesforceInput.getBaseUrl());
        logger.debug("ApiType {}", salesforceInput.getApiTypeRequested());
        logger.debug("TableName {}", salesforceInput.getTableName());
        logger.debug("UserName {}", salesforceInput.getUserName());
        logger.debug("<< logInputs(SalesforceInput)");
    }

    /**
     * A method to verify whether the input has necessary params
     * @param salesforceInput
     * Contians at least
     * 1. {@link ApiTypes} - which type of api is needed to be used
     * 2.
     * @throws MandatoryParamException
     */
    public void verifyInputs(SalesforceInput salesforceInput) throws MandatoryParamException {
        logger.debug(">> verifyInputs(SalesforceInput)");
        if (ApiTypes.USER_PWD_API.equals(salesforceInput.getApiTypeRequested())) {
            CommonUtil.checkParamForBlank(Constants.keyPassword, salesforceInput.getPwd());
            CommonUtil.checkParamForBlank(Constants.keyUsername, salesforceInput.getUserName());
            CommonUtil.checkParamForBlank(Constants.keyBaseUrl, salesforceInput.getBaseUrl());
            if (!salesforceInput.getBaseUrl().endsWith(".com")) {
                // Incorrect format
                String errorMsg = String.format("Base url needs strictly in the form %s", Constants.verifyValidBaseUrlForm);
                throw new IllegalArgumentException(errorMsg);
            }
            CommonUtil.checkParamForBlank(Constants.keyTableName, salesforceInput.getTableName());
        } else {
            // Other specific logging goes here
            // All other specific api requirements here, haven't filled any yet
        }
        logger.debug("<< verifyInputs(SalesforceInput)");
    }
}