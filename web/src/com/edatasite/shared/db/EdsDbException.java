package com.edatasite.shared.db;

/**
 * Created by Artem Pak
 * User: artem
 * Date: 02.08.2005
 * Time: 14:27:34
 * Software Team
 */

public class EdsDbException extends Exception {

    private int error = 0;

    public EdsDbException() {
    }

    public EdsDbException(String message) {
        super(message);
    }

    public EdsDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public EdsDbException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return error;
    }

}
