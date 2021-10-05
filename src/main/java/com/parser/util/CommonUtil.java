package com.parser.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parser.bean.MandatoryParamException;
import com.parser.bean.ResponseToken;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

public class CommonUtil {

    private CommonUtil() {

    }

    public static void checkParamForBlank(String paramName, String paramValue) throws MandatoryParamException {
        if (StringUtils.isBlank(paramValue)) {
            String errorMsg = String.format("Mandatory parameter %s was null/Blank", paramName);
            throw new MandatoryParamException(errorMsg);
        }
    }

}
