package com.parser.bean;

public class MandatoryParamException extends Exception {

    public MandatoryParamException(String errorMsg) {
        super(errorMsg);
    }
}
