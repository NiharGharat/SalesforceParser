package com.parser.impl;

import com.parser.bean.ReaderOutput;
import com.parser.com.parser.intf.Reader;
import com.parser.com.parser.intf.Writer;

import java.util.Map;

public class SalesforceParserImpl implements Reader, Writer {

    private SalesforceParserDao salesforceParserDao;
    public SalesforceParserImpl() {
        this.salesforceParserDao = new SalesforceParserDao();
    }

    @Override
    public ReaderOutput read(Map<String, Object> param) {
        return null;
    }

    @Override
    public void write(ReaderOutput data) {

    }
}
