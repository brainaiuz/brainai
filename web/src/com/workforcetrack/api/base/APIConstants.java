package com.workforcetrack.api.base;

import com.edatasite.shared.components.EncryptionHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 01.05.12
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public interface APIConstants {

    String ACCEPT_APPLICATION_JSON = "Accept=application/json";
    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type=application/json";
    String UNDEFINED_USER_AGENT = "undefinedUserAgent";

    int SESSION_LIVE_TIME_15_MINUTE = 15;
    long MILLISECONDS_IN_HOUR = 1000 * 60 * 60;

    String API_RESPONSE = "response";
    String API_RESULT = "result";
    String API_ERROR = "error";

    String SAVE_DATA = "saveData";

    String INTEGER_TYPE = "Integer";
    String STRING_TYPE = "String";
    String DATE_TYPE = "Date";
    String BOOLEAN_TYPE = "Boolean";
    String FLOAT_TYPE = "Boolean";

    int LIMIT_DEFAULT = 20;
    int LIMIT_MAX = 100;
    int START_DEFAULT = 0;

    String START = "start";
    String LIMIT = "limit";
    String TOTAL_COUNT = "totalCount";
    String ITEMS = "items";
    String SESSION_ID = "sessionID";

    int SESSION_ID_IS_EMPTY = 1;
    int ERROR_WITH_EXECUTING_METHOD = 2;
    int PARAMS_IS_EMPTY = 3;
    int UNKNOWN_ERROR = 4;


    String OBJECT_ID = "objectID";
    String NAME = "name";
    String NUMBER = "number";
    String DESCRIPTION = "description";

    String API_SECRET_KEY = EncryptionHelper.md5("ForeveR_KpI_WorkForCeTRACK_kPi_FOREVER");

    int MOBILE = 0;
    int OUTLOOK = 1;
    int EXCEL = 2;
    int MGWT = 3;

}
