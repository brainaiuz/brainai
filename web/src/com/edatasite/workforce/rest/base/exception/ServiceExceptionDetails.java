package com.edatasite.workforce.rest.base.exception;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 * Date: 12/20/13
 * Time: 5:19 PM
 */
public class ServiceExceptionDetails implements Serializable {

    private int code;
    private String message;

    public ServiceExceptionDetails() {
    }

    public ServiceExceptionDetails(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
