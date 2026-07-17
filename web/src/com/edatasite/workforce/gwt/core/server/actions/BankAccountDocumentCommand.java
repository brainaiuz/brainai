package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 12, 2010
 * Time: 8:12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountDocumentCommand extends WfmCommand {

    private Integer attachmentID;
    private String bankAccAttchType;
    private Integer bankAccountID;

    public Integer getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(Integer attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getBankAccAttchType() {
        return bankAccAttchType;
    }

    public void setBankAccAttchType(String bankAccAttchType) {
        this.bankAccAttchType = bankAccAttchType;
    }

    public Integer getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(Integer bankAccountID) {
        this.bankAccountID = bankAccountID;
    }
}