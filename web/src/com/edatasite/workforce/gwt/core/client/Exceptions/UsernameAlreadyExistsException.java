package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;


public class UsernameAlreadyExistsException extends WfmBaseException {

    private String msg = "Username with such email already exists";

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExistsException() {
    }

    public String getMessage() {
        return msg;
    }

}
