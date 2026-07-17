package com.edatasite.workforce.rest.base.exception;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 14, 2011
 * Time: 7:29:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeOutException extends Exception {
    private String message;

    public TimeOutException() {
    }

    public TimeOutException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
