package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 21:34:22
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "userBankAccount")
public class EdsUserBankAccount extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

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

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        if (!ServerUtils.equalsString(this.bankName, bankName) && getUser() != null) {
            getUser().addHistoryChange("Bank Name", this.bankName, bankName);
        }
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        if (!ServerUtils.equalsString(this.bankAddress, bankAddress) && getUser() != null) {
            getUser().addHistoryChange("Bank Address", this.bankAddress, bankAddress);
        }
        this.bankAddress = bankAddress;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        if (!ServerUtils.equalsString(this.accountNumber, accountNumber) && getUser() != null) {
            getUser().addHistoryChange("Account Number", this.accountNumber, accountNumber);
        }
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        if (!ServerUtils.equalsString(this.accountName, accountName) && getUser() != null) {
            getUser().addHistoryChange("Account Name", this.accountName, accountName);
        }
        this.accountName = accountName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        if (!ServerUtils.equalsString(this.swiftCode, swiftCode) && getUser() != null) {
            getUser().addHistoryChange("Swift Code", this.swiftCode, swiftCode);
        }
        this.swiftCode = swiftCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        if (!ServerUtils.equalsString(this.sortCode, sortCode) && getUser() != null) {
            getUser().addHistoryChange("Sort Code", this.sortCode, sortCode);
        }
        this.sortCode = sortCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        if (!ServerUtils.equalsString(this.ibanCode, ibanCode) && getUser() != null) {
            getUser().addHistoryChange("Iban Code", this.ibanCode, ibanCode);
        }
        this.ibanCode = ibanCode;
    }

    public void setAgentID(String agentID) {
        if (!ServerUtils.equalsString(this.agentID, agentID) && getUser() != null) {
            getUser().addHistoryChange("Agent ID", this.agentID, agentID);
        }
        this.agentID = agentID;
    }

    public String getAgentID() {
        return agentID;
    }
}
