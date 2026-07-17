package com.edatasite.workforce.rest.aspects;


/**
 * User: Dilshod Madrahimov
 * Date: 12.01.17 20:50
 */

public class CheckPermissionException extends Throwable {

    private String message;

    public CheckPermissionException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
