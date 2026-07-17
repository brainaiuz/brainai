package com.edatasite.workforce.gwt.core.client.Exceptions.base;

import com.google.gwt.user.client.rpc.SerializableException;

public abstract class WfmBaseException extends SerializableException {

    private String msg = "WfmBaseException occured.";

    public WfmBaseException() {
        super();
    }

    public abstract String getMessage();

}
