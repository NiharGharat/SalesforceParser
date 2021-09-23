package com.parser.com.parser.intf;

import com.parser.bean.ReaderOutput;

import java.util.Map;

public interface Reader {

    ReaderOutput read(Map<String, Object> param);
}
