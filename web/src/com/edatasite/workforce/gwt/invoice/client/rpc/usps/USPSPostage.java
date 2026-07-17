package com.edatasite.workforce.gwt.invoice.client.rpc.usps;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/13/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSPostage implements IsSerializable{
    private String mailService;
    private BigDecimal amount;
    private String classID;
    public USPSPostage() {
    }

    public String getMailService() {
        return mailService;
    }

    public void setMailService(String mailService) {
        this.mailService = mailService;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
