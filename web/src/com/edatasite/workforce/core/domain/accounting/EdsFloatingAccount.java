package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 12.06.2009
 * Time: 14:49:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "floatingAccount")
public class EdsFloatingAccount extends EdsAccount {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debitTypeId")
    @ForeignKey(name = "none")
    private EdsAccountType debitType;//EXPENSE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditTypeId")
    @ForeignKey(name = "none")
    private EdsAccountType creditType;//REVENUE

    public EdsAccountType getDebitType() {
        return debitType;
    }

    public void setDebitType(EdsAccountType debitType) {
        this.debitType = debitType;
    }

    public EdsAccountType getCreditType() {
        return creditType;
    }

    public void setCreditType(EdsAccountType creditType) {
        this.creditType = creditType;
    }
}
