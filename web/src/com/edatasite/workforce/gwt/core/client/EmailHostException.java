package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;


public class EmailHostException extends WfmBaseException {

    private String msg = "Invalid email address";

    private static final long serialVersionUID = 1L;

    public EmailHostException() {
    }

    public String getMessage() {
        return msg;
    }

}