package com.edatasite.workforce.gwt.core.client.Exceptions;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;

/**
 * Created by Azam on 24.04.2020.
 */

public class UsersLimitExceededException extends WfmBaseException {

    private String msg = "Sorry, you have exceeded your users limit. If you would like to add new users please increase your users limit in My account";

    private static final long serialVersionUID = 1L;

    public UsersLimitExceededException() {
    }

    public String getMessage() {
        return msg;
    }
}