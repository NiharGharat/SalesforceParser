package com.parser.util;

import com.parser.bean.ApiTypes;
import com.parser.bean.SalesforceInput;
import com.parser.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SalesforceInputUtil {

    private static Logger logger = LoggerFactory.getLogger(SalesforceInputUtil.class);

    public static SalesforceInput createInput(Map<String, Object> param) {
        logger.info(">> createInput()");
        SalesforceInput salesforceInput = SalesforceInput.builder()
                        .apiTypeRequested(ApiTypes.valueOf(param.getOrDefault(Constants.keyApiTypeRequest, ApiTypes.USER_PWD_API).toString().toUpperCase()))
                .baseUrl(param.getOrDefault(Constants.keyBaseUrl, Constants.DEFAULT_LOGIN_URL).toString())
                .pwd(param.getOrDefault(Constants.keyPassword, "").toString())
                .tableName(param.get(Constants.keyTableName).toString())
                .userName(param.getOrDefault(Constants.keyUsername, "").toString())
                .build();
        logger.info("<< createInput()");
        return salesforceInput;
    }
}
