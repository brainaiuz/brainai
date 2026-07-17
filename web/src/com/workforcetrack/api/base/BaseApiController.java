package com.workforcetrack.api.base;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 01.05.12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */

public interface BaseApiController<T> {

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

    @RequestMapping(value = "/fields", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @ResponseBody
    T getEntityFieldsNameList();

    //@RequestMapping(value = "/list", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
/*    @ResponseBody
    T getList(@RequestBody Map<String, Object> params);*/

    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @ResponseBody
    T get(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @ResponseBody
    T save(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "/delete", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @ResponseBody
    T delete(@RequestBody Map<String, Object> params);

}
