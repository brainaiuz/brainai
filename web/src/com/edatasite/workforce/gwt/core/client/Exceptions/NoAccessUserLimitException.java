package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;

public class NoAccessUserLimitException extends WfmBaseException {
    private String msg = "Sorry, you have exceeded your no access users limit";

    private static final long serialVersionUID = 1L;

    public NoAccessUserLimitException() {
    }
    @Override
    public String getMessage() {
        return msg;
    }
}
