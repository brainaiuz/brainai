package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 9/27/2017.
 */
public class ApiResult extends ResponseData {
    private Boolean success;
    private ResponseData data;
    private ErrorTO error;

    public ApiResult() {
    }

    public ApiResult(ResponseData data) {
        this.data = data;
        success = Boolean.TRUE;
    }

    public ApiResult(String user_msg, String developer_msg, Integer error_code) {
        this.success = false;
        this.data = new ResponseData();
        this.error = new ErrorTO(user_msg, developer_msg, error_code);
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    public ErrorTO getError() {
        return error;
    }

    public void setError(ErrorTO error) {
        this.error = error;
    }
}
