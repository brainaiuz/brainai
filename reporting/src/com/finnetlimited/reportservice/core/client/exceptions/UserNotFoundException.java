package com.finnetlimited.reportservice.core.client.exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 11.06.2010
 * Time: 20:13:54
 * To change this template use File | Settings | File Templates.
 */
public class UserNotFoundException extends SerializableException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
