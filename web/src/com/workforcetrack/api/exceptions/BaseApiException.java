package com.workforcetrack.api.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 15.05.12
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class BaseApiException extends Exception {

    private int code = -1;

    public BaseApiException(String message) {
        super(message);
    }

    public BaseApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseApiException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }
}
