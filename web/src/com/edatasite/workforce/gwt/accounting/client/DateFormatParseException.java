package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.core.client.Exceptions.base.WfmBaseException;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12.06.2010
 * Time: 13:28:05
 * To change this template use File | Settings | File Templates.
 */
public class DateFormatParseException extends WfmBaseException {
    private String message;

    public DateFormatParseException() {
    }

    public DateFormatParseException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
