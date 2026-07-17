package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsVat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 23.02.2009
 * Time: 12:32:14
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "accounttemplate")
public class EdsAccountTemplate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountTypeId")
    private EdsAccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsVat tax;

    @Column(unique = true)
    private Integer code;

    @Column(unique = true)
    private Integer key;

    private String codeString;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCountry country;

    private Boolean showInExpense = false;
    private Boolean enablePayments = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeString() {
        return codeString;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public EdsAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(EdsAccountType accountType) {
        this.accountType = accountType;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShowInExpense() {
        return showInExpense;
    }

    public void setShowInExpense(Boolean showInExpense) {
        this.showInExpense = showInExpense;
    }

    public Boolean getEnablePayments() {
        return enablePayments;
    }

    public void setEnablePayments(Boolean enablePayments) {
        this.enablePayments = enablePayments;
    }
}
