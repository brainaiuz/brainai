package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 4:35:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserNotFoundException extends SerializableException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
