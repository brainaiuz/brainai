package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 9/27/2017.
 */
public class ErrorTO extends ResponseData {
    private String user_msg;
    private String developer_msg;
    private Integer error_code;

    public ErrorTO() {
    }

    public ErrorTO(String user_msg, String developer_msg, Integer error_code) {
        this.user_msg = user_msg;
        this.developer_msg = developer_msg;
        this.error_code = error_code;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getDeveloper_msg() {
        return developer_msg;
    }

    public void setDeveloper_msg(String developer_msg) {
        this.developer_msg = developer_msg;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }
}
