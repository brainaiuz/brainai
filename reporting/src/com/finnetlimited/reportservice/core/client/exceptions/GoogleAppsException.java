package com.finnetlimited.reportservice.core.client.exceptions;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 16:17:32
 * To change this template use File | Settings | File Templates.
 */
public class GoogleAppsException extends Exception implements Serializable {

    public GoogleAppsException() {
        super();
    }

    public GoogleAppsException(String message) {
        super(message);
    }
}
