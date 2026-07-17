package com.edatasite.workforce.rest.v2.release10.exp;

import org.springframework.http.HttpStatus;

/**
 * Created by Dilsh0d Madrahimov on 10/4/2017.
 */
public class RestException extends Exception {

    private final String user_msg;
    private final String developer_msg;
    private final Integer error_code;
    private final HttpStatus status;

    public RestException(String user_msg, String developer_msg, Integer error_code, HttpStatus status) {
        this.user_msg = user_msg;
        this.developer_msg = developer_msg;
        this.error_code = error_code;
        this.status = status;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public String getDeveloper_msg() {
        return developer_msg;
    }

    public Integer getError_code() {
        return error_code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
