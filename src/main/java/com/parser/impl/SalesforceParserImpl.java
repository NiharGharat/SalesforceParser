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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public ReaderOutput read(Map<String, Object> param) throws MandatoryParamException {
        logger.info(">> read(param)");
        // Map the params to salesforceInput class
        SalesforceInput salesforceInput = SalesforceInputUtil.createInput(param);
        // Verify min inputs required
        verifyInputs(salesforceInput);
        // Log necessary once them
        logInputs(salesforceInput);
        // Pass the input to dao and get result class
        ReaderOutput readerOutput = dao.readDao(salesforceInput);
        logger.info("<< read(param)");
        return readerOutput;
    }

    private void logInputs(SalesforceInput salesforceInput) {
        logger.info(">> logInputs(salesforceInput)");
        logger.info("BaseUrl {}", salesforceInput.getBaseUrl());
        logger.info("ApiType {}", salesforceInput.getApiTypeRequested());
        logger.info("TableName {}", salesforceInput.getTableName());
        logger.info("UserName {}", salesforceInput.getUserName());
        logger.info("<< logInputs(salesforceInput)");
    }

    private void verifyInputs(SalesforceInput salesforceInput) throws MandatoryParamException {
        logger.info(">> verifyInputs(salesforceInput)");
        if (ApiTypes.USER_PWD_API.equals(salesforceInput.getApiTypeRequested())) {
            CommonUtil.checkParamForBlank(Constants.keyPassword, salesforceInput.getPwd());
            CommonUtil.checkParamForBlank(Constants.keyUsername, salesforceInput.getUserName());
        } else {
            // Other specific logging goes here
        }
        CommonUtil.checkParamForBlank(Constants.keyBaseUrl, salesforceInput.getBaseUrl());
        CommonUtil.checkParamForBlank(Constants.keyTableName, salesforceInput.getTableName());
        logger.info("<< verifyInputs(salesforceInput)");
    }
}