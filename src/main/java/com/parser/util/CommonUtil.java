package com.parser.util;

import com.parser.bean.MandatoryParamException;
import org.apache.commons.lang3.StringUtils;

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
