package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 4:36:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class IncorrectPasswordException extends SerializableException {

    public IncorrectPasswordException() {
    }

    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
