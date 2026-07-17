package com.edatasite.workforce.gwt.core.client.Exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 20.11.2010
 * Time: 23:03:47
 * To change this template use File | Settings | File Templates.
 */
public class NumberExistingException extends Exception {
    private String detailedMessage;

    public NumberExistingException() {
    }

    public NumberExistingException(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }
}
