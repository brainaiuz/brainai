package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;

/**
 * Created by Shohruh on 28 May 2017.
 */
public class EmployeeCodeExistsException extends WfmBaseException {

    private String msg = "You already have a employee with this employee code";

    private static final long serialVersionUID = 1L;

    public EmployeeCodeExistsException() {
    }

    public String getMessage() {
        return msg;
    }

}
