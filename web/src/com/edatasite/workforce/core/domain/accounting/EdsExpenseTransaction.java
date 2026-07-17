package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsExpenseReport;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 25.04.2009
 * Time: 16:29:36
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expenseTransaction")
public class EdsExpenseTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseReportid")
    private EdsExpenseReport expenseReport;

    public EdsExpenseReport getExpenseReport() {
        return expenseReport;
    }

    public void setExpenseReport(EdsExpenseReport expenseReport) {
        this.expenseReport = expenseReport;

        setCurrencyID(expenseReport.getCurrency() != null ? expenseReport.getCurrency().getObjectID() : null);
        setExchangeRate(expenseReport.getExchangeRate());
    }

    public Integer getKeyId() {
        if (getExpenseReport() != null) {
            return getExpenseReport().getObjectID();
        } else if (getReversalTransaction() != null && getReversalTransaction() instanceof EdsExpenseTransaction
                && ((EdsExpenseTransaction) getReversalTransaction()).getExpenseReport() != null) {
            return ((EdsExpenseTransaction) getReversalTransaction()).getExpenseReport().getObjectID();
        }
        return null;
    }

    public String getKeyType() {
        return EXPENSE_TRANSACTION;
    }
}
