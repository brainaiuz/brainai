package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 6/23/11
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "typeItem")
public class MTypeItem extends MSelectItem {

    private String code;
    private Integer currencyID;

    private Integer paymentTypeID;//Payment Method ID
    private String paymentType; //Payment Method Name

    private String currency;
    private String encryptedLink;
    private Date dueDate;
    private Double dueAmount;
    private String status;

    private Integer billAddressID;
    private Integer mailAddressID;

    public MTypeItem() {
    }

    public MTypeItem(TypeItem typeItem) {
        if (typeItem == null){
            return;
        }
        this.objectID = typeItem.getId();
        this.name = typeItem.getName();
        this.description = typeItem.getDescription();
        this.code = typeItem.getCode();
        this.currencyID = typeItem.getCurrencyID();
        this.paymentTypeID = typeItem.getPaymentTypeID();
        this.paymentType = typeItem.getPaymentType();
        this.currency = typeItem.getCurrency();
        this.encryptedLink = typeItem.getEncryptedLink();
        this.dueDate = typeItem.getDueDate() != null ? typeItem.getDueDate().getNonConvertedDate() : null;
        this.dueAmount = typeItem.getDueAmount();
        this.status = typeItem.getStatus();

        this.billAddressID = typeItem.getBillAddressID();
        this.mailAddressID = typeItem.getMailAddressID();
    }

    public TypeItem convertToTypeItem(TypeItem typeItem) {
        if (typeItem == null){
            typeItem = new TypeItem();
        }
        typeItem.setId(this.getObjectID());
        typeItem.setName(this.getName());
        typeItem.setDescription(this.getDescription());
        typeItem.setCode(this.getCode());
        typeItem.setCurrencyID(this.getCurrencyID());
        typeItem.setPaymentTypeID(this.getPaymentTypeID());
        typeItem.setPaymentType(this.getPaymentType());
        typeItem.setCurrency(this.getCurrency());
        typeItem.setEncryptedLink(this.getEncryptedLink());
        typeItem.setDueDate(this.getDueDate() != null ? new DateNonConvertable(this.getDueDate()) : null);
        typeItem.setDueAmount(this.getDueAmount());
        typeItem.setStatus(this.getStatus());
        typeItem.setBillAddressID(this.getBillAddressID());
        return typeItem;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(Integer paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEncryptedLink() {
        return encryptedLink;
    }

    public void setEncryptedLink(String encryptedLink) {
        this.encryptedLink = encryptedLink;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBillAddressID() {
        return billAddressID;
    }

    public void setBillAddressID(Integer billAddressID) {
        this.billAddressID = billAddressID;
    }

    public Integer getMailAddressID() {
        return mailAddressID;
    }

    public void setMailAddressID(Integer mailAddressID) {
        this.mailAddressID = mailAddressID;
    }

}
