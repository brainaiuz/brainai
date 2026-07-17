package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 13, 2010
 * Time: 12:58:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountAttachment implements IsSerializable {
    private Integer objectID;
    private String name;
    private Integer attachmentID;
    private String bankAccAttchType;
    private Integer bankAccountID;
    private Boolean imported;
    private Boolean reconciled;

    public BankAccountAttachment() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Boolean isImported() {
        return imported;
    }

    public void setImported(Boolean imported) {
        this.imported = imported;
    }

    public Boolean getReconciled() {
        return reconciled;
    }

    public void setReconciled(Boolean reconciled) {
        this.reconciled = reconciled;
    }
}