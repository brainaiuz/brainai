package com.edatasite.workforce.rest.base.exception;

/**
 * User: Sher
 * Date: 4/25/13 2:55 PM
 */
public class Exceptions {
    public static ServiceExceptionDetails custom(String message) {
        return new ServiceExceptionDetails(999, message);
    }
}
