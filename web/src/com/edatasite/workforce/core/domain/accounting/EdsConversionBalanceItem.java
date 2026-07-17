package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
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
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 19.02.2009
 * Time: 13:17:30
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "conversionBalanceItem")
//looks like a EdsTransactionItem
public class EdsConversionBalanceItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(precision = 25, scale = 5)
    private BigDecimal debit;
    @Column(precision = 25, scale = 5)
    private BigDecimal credit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversionBalanceId")
    private EdsConversionBalance conversionBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    //Manual Journal related
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    private EdsVat tax;

    //Manual Journal related
    private String description;


    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
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

    public EdsConversionBalance getConversionBalance() {
        return conversionBalance;
    }

    public void setConversionBalance(EdsConversionBalance conversionBalance) {
        this.conversionBalance = conversionBalance;
    }
}
