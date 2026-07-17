package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 03.03.2009
 * Time: 17:37:57
 * To change this template use File | Settings | File Templates.
 */
public class TotalDebitCredit {

    public BigDecimal credit = AccountingConstants.ZERO;
    public BigDecimal creditCurrentPeriod = AccountingConstants.ZERO;
    public BigDecimal debitCurrentPeriod = AccountingConstants.ZERO;
    public BigDecimal creditInBase = AccountingConstants.ZERO;
    public BigDecimal debit = AccountingConstants.ZERO;
    public BigDecimal debitInBase = AccountingConstants.ZERO;

    public BigDecimal interCompanyCredit = AccountingConstants.ZERO;
    public BigDecimal interCompanyDebit = AccountingConstants.ZERO;

    private boolean containsTransaction;
    private boolean containsInterCompany;

    private String accountTypeCode;
    private Integer accountID;
    private Integer currencyID;

    public TotalDebitCredit() {
    }

    public TotalDebitCredit(BigDecimal debit, BigDecimal credit) {
        this.credit = credit;
        this.debit = debit;
    }

    public byte dcCompare() {
        if (debit.compareTo(credit) >= 0) {
            return EdsAccount.DEBIT;
        } else {
            return EdsAccount.CREDIT;
        }
    }
    public byte dcInterCompanyCompare() {
        if (interCompanyDebit.compareTo(interCompanyCredit) >= 0) {
            return EdsAccount.DEBIT;
        } else {
            return EdsAccount.CREDIT;
        }
    }

    public BigDecimal getDebitCreditDiff() {
        return debit.subtract(credit).abs();
    }

    public BigDecimal getInterCompanyDebitCreditDiff() {
        return interCompanyDebit.subtract(interCompanyCredit).abs();
    }

    public BigDecimal getRealDebitCreditDiff() {
        return debit.subtract(credit);
    }

    public BigDecimal getRealDebitCreditDiffInBase() {
        return debitInBase.subtract(creditInBase);
    }

    public BigDecimal getInterCompanyRealDebitCreditDiff() {
        return interCompanyDebit.subtract(interCompanyCredit);
    }

    public BigDecimal getPNLRealDifference() {
        if (EdsAccountType.COST_OF_SALES.equals(accountTypeCode) || EdsAccountType.DIRECT_EXPENSES.equals(accountTypeCode)
                || EdsAccountType.DEPRECIATION.equals(accountTypeCode) || EdsAccountType.OVERHEAD.equals(accountTypeCode)) {
            return getRealDebitCreditDiff();
        } else {
            return BigDecimal.ZERO.subtract(getRealDebitCreditDiff());
        }
    }

    public BigDecimal getPNLInterCompanyRealDifference() {
        if (EdsAccountType.COST_OF_SALES.equals(accountTypeCode) || EdsAccountType.DIRECT_EXPENSES.equals(accountTypeCode)
                || EdsAccountType.DEPRECIATION.equals(accountTypeCode) || EdsAccountType.OVERHEAD.equals(accountTypeCode)) {
            return getInterCompanyRealDebitCreditDiff();
        } else {
            return BigDecimal.ZERO.subtract(getInterCompanyRealDebitCreditDiff());
        }
    }

    public static TotalDebitCredit solve(BigDecimal debit, BigDecimal credit) {
        return new TotalDebitCredit(debit, credit);
    }

    public TotalDebitCredit add(TotalDebitCredit dc) {
        if (dc != null) {
            debit = debit.add(dc.debit);
            credit = credit.add(dc.credit);
        }
        return this;
    }

    public TotalDebitCredit subtract(TotalDebitCredit dc) {
        if (dc != null) {
            debit = debit.subtract(dc.debit);
            credit = credit.subtract(dc.credit);
        }
        return this;
    }

    public TotalDebitCredit multiply(BigDecimal exchangeRate) {
        if (exchangeRate != null) {
            debit = debit.multiply(exchangeRate);
            credit = credit.multiply(exchangeRate);
        }
        return this;
    }

    public TotalDebitCredit divide(BigDecimal exchangeRate, int scale, int rounding) {
        if (exchangeRate != null) {
            debit = debit.divide(exchangeRate, scale, rounding);
            credit = credit.divide(exchangeRate, scale, rounding);
        }
        return this;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public boolean isContainsTransaction() {
        return containsTransaction;
    }

    public void setContainsTransaction(boolean containsTransaction) {
        this.containsTransaction = containsTransaction;
    }

    public boolean isContainsInterCompany() {
        return containsInterCompany;
    }

    public void setContainsInterCompany(boolean containsInterCompany) {
        this.containsInterCompany = containsInterCompany;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }
}
