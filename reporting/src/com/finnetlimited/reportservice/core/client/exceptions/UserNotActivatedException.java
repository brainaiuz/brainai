package com.finnetlimited.reportservice.core.client.exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 11.06.2010
 * Time: 20:11:49
 * To change this template use File | Settings | File Templates.
 */
public class UserNotActivatedException extends SerializableException {
    public UserNotActivatedException() {
    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
