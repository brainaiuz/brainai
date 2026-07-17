package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 13, 2010
 * Time: 2:43:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountTransactionMapper implements IsSerializable {
    private Integer bankAccountID;
    private Integer bankAccountAttachmentID;
    private String fileColumnName;
    private String fileColumnValue;
    private Integer fileColumnIndex;
    private Integer transactionField;

    public BankAccountTransactionMapper() {
    }

    public Integer getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(Integer bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public Integer getBankAccountAttachmentID() {
        return bankAccountAttachmentID;
    }

    public void setBankAccountAttachmentID(Integer bankAccountAttachmentID) {
        this.bankAccountAttachmentID = bankAccountAttachmentID;
    }

    public String getFileColumnName() {
        return fileColumnName;
    }

    public void setFileColumnName(String fileColumnName) {
        this.fileColumnName = fileColumnName;
    }

    public String getFileColumnValue() {
        return fileColumnValue;
    }

    public void setFileColumnValue(String fileColumnValue) {
        this.fileColumnValue = fileColumnValue;
    }

    public Integer getFileColumnIndex() {
        return fileColumnIndex;
    }

    public void setFileColumnIndex(Integer fileColumnIndex) {
        this.fileColumnIndex = fileColumnIndex;
    }

    public Integer getTransactionField() {
        return transactionField;
    }

    public void setTransactionField(Integer transactionField) {
        this.transactionField = transactionField;
    }
}
