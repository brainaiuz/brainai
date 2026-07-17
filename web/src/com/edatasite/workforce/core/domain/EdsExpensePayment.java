package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 23, 2009
 * Time: 3:44:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expensePayments")
public class EdsExpensePayment extends EdsPaymentBase {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseReportId")
    private EdsExpenseReport expenseReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private EdsCrmAccount supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsPayslipTableItem payslipTableItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchpaymentid")
    private EdsBatchPayment batchPayment;

    @Column(precision = 25, scale = 5)
    private BigDecimal amountInEntityCurrency;

    public EdsExpenseReport getExpenseReport() {
        return expenseReport;
    }

    public void setExpenseReport(EdsExpenseReport expenseReport) {
        this.expenseReport = expenseReport;
    }

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    public EdsPayslipTableItem getPayslipTableItem() {
        return payslipTableItem;
    }

    public void setPayslipTableItem(EdsPayslipTableItem payslipTableItem) {
        this.payslipTableItem = payslipTableItem;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsBatchPayment getBatchPayment() {
        return batchPayment;
    }

    public void setBatchPayment(EdsBatchPayment batchPayment) {
        this.batchPayment = batchPayment;
    }

    public BigDecimal getAmountInEntityCurrency() {
        return amountInEntityCurrency;
    }

    public void setAmountInEntityCurrency(BigDecimal amountInEntityCurrency) {
        this.amountInEntityCurrency = amountInEntityCurrency;
    }

    public ExpensePaymentData getExpensePaymentAsRPC() {
        ExpensePaymentData data = new ExpensePaymentData();
        data.setObjectID(getObjectID());
        data.setReportId(getExpenseReport().getObjectID());
        data.setDate(new DateNonConvertable(getPaymentDate()));
        data.setPaymentAccount(getAccount() != null ? getAccount().getAsSelectItem() : null);
        data.setPaymentAmount(getAmountInEntityCurrency() != null ? getAmountInEntityCurrency() : getAmount());
        data.setReferenceNumber(getReference());
        data.setExchangeRate(getExchangeRate());
        data.setStatus(getStatus() != null ? getStatus().getName() : null);
        if (getSupplier() != null) {
            data.setSupplier(getSupplier().getAsSelectItem());
        }
        return data;
    }
}
