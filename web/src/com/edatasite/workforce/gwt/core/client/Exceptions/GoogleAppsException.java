package com.edatasite.workforce.gwt.core.client.Exceptions;

import java.io.Serializable;

/**
 * User: Ilhombek
 * Date: 18.06.2010
 * Time: 20:37:39
 */
public class GoogleAppsException extends Exception implements Serializable {

    public GoogleAppsException() {
        super();
    }

    public GoogleAppsException(String message) {
        super(message);
    }
}
