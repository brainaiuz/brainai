package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 05.08.2010
 * Time: 14:34:53
 * To change this template use File | Settings | File Templates.
 */
public class UserBankAccountData implements IsSerializable {
    private Integer objectID;
    private String bankName;
    private String bankAddress;
    private String accountNumber;
    private String accountName;
    private String swiftCode;
    private String sortCode;
    private String ibanCode;
    private String agentID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getAgentID() {
        return agentID;
    }
}
