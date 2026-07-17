package com.finnetlimited.reportservice.core.client.exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 11.06.2010
 * Time: 20:14:26
 * To change this template use File | Settings | File Templates.
 */
public class IncorrectPasswordException extends SerializableException {

    public IncorrectPasswordException() {
    }

    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
