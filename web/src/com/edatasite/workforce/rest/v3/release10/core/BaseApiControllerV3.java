package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ErrorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.regex.Pattern;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 5:39 PM
 */

/**
 *
 */
public class BaseApiControllerV3 implements ApiConstants {

    protected Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([_.-]\\w+)*@(\\w+([_.-]\\w+)*)");

    protected static ApiResult successResponse(ResponseData data) {
        ApiResult result = new ApiResult();
        result.setSuccess(Boolean.TRUE);
        result.setData(data);
        result.setError(new ErrorTO());
        return result;
    }
}
