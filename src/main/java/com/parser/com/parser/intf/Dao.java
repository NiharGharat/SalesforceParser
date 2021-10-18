package com.parser.com.parser.intf;

import com.parser.bean.ReaderOutput;
import com.parser.bean.SalesforceInput;

/*
// TODO
Marker for Dao impls
Similar to RequestInput
 */
public interface Dao {

    public ReaderOutput readDao(RequestInput requestInput) throws Exception;
}
