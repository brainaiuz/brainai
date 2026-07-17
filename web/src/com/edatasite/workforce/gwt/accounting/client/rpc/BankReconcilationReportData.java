package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.07.2010
 * Time: 16:28:50
 * To change this template use File | Settings | File Templates.
 */
public class BankReconcilationReportData implements IsSerializable {

    private BankReconcilationReportItem[] outstandingPayments;
    private BankReconcilationReportItem[] outstandingReceipts;
    private BankReconcilationReportItem[] unReconciledBankStatementLines;
    private BigDecimal totalOutstandingPayments;
    private BigDecimal totalOutstandingReceipts;
    private BigDecimal totalUnReconciledBankStatementLines;

    private BigDecimal bankAccountBalanceInWFT;
    private BigDecimal balanceAtBank;
    private BigDecimal reconcileBalance;

    public BankReconcilationReportData() {
    }

    public BankReconcilationReportItem[] getOutstandingPayments() {
        return outstandingPayments;
    }

    public void setOutstandingPayments(BankReconcilationReportItem[] outstandingPayments) {
        this.outstandingPayments = outstandingPayments;
    }

    public BankReconcilationReportItem[] getOutstandingReceipts() {
        return outstandingReceipts;
    }

    public void setOutstandingReceipts(BankReconcilationReportItem[] outstandingReceipts) {
        this.outstandingReceipts = outstandingReceipts;
    }

    public BankReconcilationReportItem[] getUnReconciledBankStatementLines() {
        return unReconciledBankStatementLines;
    }

    public void setUnReconciledBankStatementLines(BankReconcilationReportItem[] unReconciledBankStatementLines) {
        this.unReconciledBankStatementLines = unReconciledBankStatementLines;
    }

    public BigDecimal getTotalOutstandingPayments() {
        return totalOutstandingPayments;
    }

    public void setTotalOutstandingPayments(BigDecimal totalOutstandingPayments) {
        this.totalOutstandingPayments = totalOutstandingPayments;
    }

    public BigDecimal getTotalOutstandingReceipts() {
        return totalOutstandingReceipts;
    }

    public void setTotalOutstandingReceipts(BigDecimal totalOutstandingReceipts) {
        this.totalOutstandingReceipts = totalOutstandingReceipts;
    }

    public BigDecimal getTotalUnReconciledBankStatementLines() {
        return totalUnReconciledBankStatementLines;
    }

    public void setTotalUnReconciledBankStatementLines(BigDecimal totalUnReconciledBankStatementLines) {
        this.totalUnReconciledBankStatementLines = totalUnReconciledBankStatementLines;
    }

    public BigDecimal getBankAccountBalanceInWFT() {
        return bankAccountBalanceInWFT;
    }

    public void setBankAccountBalanceInWFT(BigDecimal bankAccountBalanceInWFT) {
        this.bankAccountBalanceInWFT = bankAccountBalanceInWFT;
    }

    public BigDecimal getBalanceAtBank() {
        return balanceAtBank;
    }

    public void setBalanceAtBank(BigDecimal balanceAtBank) {
        this.balanceAtBank = balanceAtBank;
    }

    public BigDecimal getReconcileBalance() {
        return reconcileBalance;
    }

    public void setReconcileBalance(BigDecimal reconcileBalance) {
        this.reconcileBalance = reconcileBalance;
    }
}
