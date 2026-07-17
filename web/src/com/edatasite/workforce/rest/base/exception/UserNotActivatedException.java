package com.edatasite.workforce.rest.base.exception;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 31.10.2008
 * Time: 15:30:27
 * To change this template use File | Settings | File Templates.
 */
public class UserNotActivatedException extends Exception {
    public UserNotActivatedException() {
    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
