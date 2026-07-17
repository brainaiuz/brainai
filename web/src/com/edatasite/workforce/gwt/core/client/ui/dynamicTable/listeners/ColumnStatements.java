package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.12.2008
 * Time: 18:03:29
 * To change this template use File | Settings | File Templates.
 */
public class ColumnStatements {

    private String infoMessage;
    private String notValidMessage;

    public ColumnStatements(String infoMessage, String notValidMessage) {

        this.infoMessage = infoMessage;
        this.notValidMessage = notValidMessage;
    }

    public ColumnStatements(String message, boolean validate) {

        setMessage(message, validate);
    }

    private void setMessage(String message, boolean validate) {

        if (validate) {
            this.notValidMessage = message;
        } else {
            this.infoMessage = message;
        }
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public String getNotValidMessage() {
        return notValidMessage;
    }
}
