package com.parser.impl;

import com.parser.bean.ReaderOutput;
import com.parser.bean.SalesforceInput;
import com.parser.com.parser.intf.Reader;
import com.parser.com.parser.intf.Writer;
import com.parser.util.SalesforceInputUtil;

import java.util.Map;

public class SalesforceParserImpl implements Reader, Writer {

    private Dao dao;
    public SalesforceParserImpl() {
        this.dao = new Dao();
    }

    @Override
    public void write(ReaderOutput data) {
        return;
    }

    // Init values to SalesforceInput object
    // Pass the object to Dao for proc
    // Get the list data
    // Return
    @Override
    public ReaderOutput read(Map<String, Object> param) {
        SalesforceInput salesforceInput = SalesforceInputUtil.createInput(param);
        verifyInputs(salesforceInput);
        logInputs(salesforceInput);
        ReaderOutput readerOutput = dao.readDao(salesforceInput);
        return readerOutput;
    }

    private void logInputs(SalesforceInput salesforceInput) {

    }

    private void verifyInputs(SalesforceInput salesforceInput) {

    }
}
