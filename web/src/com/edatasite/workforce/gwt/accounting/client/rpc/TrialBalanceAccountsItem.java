package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.TotalDebitCredit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Akhror
 * Date: 23.12.2020
 * Time: 18:10:55
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalanceAccountsItem {
    private EdsAccount account;
    private boolean summary;
    private ArrayList<EdsAccount> accounts;

    private HashMap<Integer, TotalDebitCredit> debitCreditMap;
    private HashMap<Integer, TotalDebitCredit> beginningBalanceMap;
    private HashMap<Integer, TotalDebitCredit> foreignBeginningBalanceMap;

    private Integer currencyId;
    private Integer baseCurrencyId;
    private Integer exchangeRateScale;

    private BigDecimal prevExchangeRate;
    private BigDecimal exchangeRate;

    private Date prevBalanceDate;
    private Date toDate;

    private HashMap<Integer, TotalDebitCredit> foreignDebitCreditMap;
    private TotalDebitCredit gainLoss;

    private Integer showAccounts;
    private LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList;
    private TotalDebitCredit reTotalDebitCredit;

    private HashMap<String, BigDecimal> trailBalanceTotalMap;
    private LinkedHashMap<String, TrialBalanceItem> tempAccountByTypeMapSummary;
    private HashMap<Integer, TrialBalanceItem> tempAccountByTypeMap;
    private boolean parent = false;

    public TrialBalanceAccountsItem() {
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public ArrayList<EdsAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<EdsAccount> accounts) {
        this.accounts = accounts;
    }

    public HashMap<Integer, TotalDebitCredit> getDebitCreditMap() {
        return debitCreditMap;
    }

    public void setDebitCreditMap(HashMap<Integer, TotalDebitCredit> debitCreditMap) {
        this.debitCreditMap = debitCreditMap;
    }

    public HashMap<Integer, TotalDebitCredit> getBeginningBalanceMap() {
        return beginningBalanceMap;
    }

    public void setBeginningBalanceMap(HashMap<Integer, TotalDebitCredit> beginningBalanceMap) {
        this.beginningBalanceMap = beginningBalanceMap;
    }

    public HashMap<Integer, TotalDebitCredit> getForeignBeginningBalanceMap() {
        return foreignBeginningBalanceMap;
    }

    public void setForeignBeginningBalanceMap(HashMap<Integer, TotalDebitCredit> foreignBeginningBalanceMap) {
        this.foreignBeginningBalanceMap = foreignBeginningBalanceMap;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Integer getExchangeRateScale() {
        return exchangeRateScale;
    }

    public void setExchangeRateScale(Integer exchangeRateScale) {
        this.exchangeRateScale = exchangeRateScale;
    }

    public BigDecimal getPrevExchangeRate() {
        return prevExchangeRate;
    }

    public void setPrevExchangeRate(BigDecimal prevExchangeRate) {
        this.prevExchangeRate = prevExchangeRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getPrevBalanceDate() {
        return prevBalanceDate;
    }

    public void setPrevBalanceDate(Date prevBalanceDate) {
        this.prevBalanceDate = prevBalanceDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public HashMap<Integer, TotalDebitCredit> getForeignDebitCreditMap() {
        return foreignDebitCreditMap;
    }

    public void setForeignDebitCreditMap(HashMap<Integer, TotalDebitCredit> foreignDebitCreditMap) {
        this.foreignDebitCreditMap = foreignDebitCreditMap;
    }

    public TotalDebitCredit getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(TotalDebitCredit gainLoss) {
        this.gainLoss = gainLoss;
    }

    public Integer getShowAccounts() {
        return showAccounts;
    }

    public void setShowAccounts(Integer showAccounts) {
        this.showAccounts = showAccounts;
    }

    public LinkedHashMap<String, LinkedList<TrialBalanceItem>> getMapAccountTypeByList() {
        return mapAccountTypeByList;
    }

    public void setMapAccountTypeByList(LinkedHashMap<String, LinkedList<TrialBalanceItem>> mapAccountTypeByList) {
        this.mapAccountTypeByList = mapAccountTypeByList;
    }

    public TotalDebitCredit getReTotalDebitCredit() {
        return reTotalDebitCredit;
    }

    public void setReTotalDebitCredit(TotalDebitCredit reTotalDebitCredit) {
        this.reTotalDebitCredit = reTotalDebitCredit;
    }

    public HashMap<String, BigDecimal> getTrailBalanceTotalMap() {
        return trailBalanceTotalMap;
    }

    public void setTrailBalanceTotalMap(HashMap<String, BigDecimal> trailBalanceTotalMap) {
        this.trailBalanceTotalMap = trailBalanceTotalMap;
    }

    public LinkedHashMap<String, TrialBalanceItem> getTempAccountByTypeMapSummary() {
        return tempAccountByTypeMapSummary;
    }

    public void setTempAccountByTypeMapSummary(LinkedHashMap<String, TrialBalanceItem> tempAccountByTypeMapSummary) {
        this.tempAccountByTypeMapSummary = tempAccountByTypeMapSummary;
    }

    public HashMap<Integer, TrialBalanceItem> getTempAccountByTypeMap() {
        return tempAccountByTypeMap;
    }

    public void setTempAccountByTypeMap(HashMap<Integer, TrialBalanceItem> tempAccountByTypeMap) {
        this.tempAccountByTypeMap = tempAccountByTypeMap;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrialBalanceAccountsItem)) return false;

        TrialBalanceAccountsItem that = (TrialBalanceAccountsItem) o;

        if (isSummary() != that.isSummary()) return false;
        if (isParent() != that.isParent()) return false;
        if (getAccount() != null ? !getAccount().equals(that.getAccount()) : that.getAccount() != null) return false;
        if (getAccounts() != null ? !getAccounts().equals(that.getAccounts()) : that.getAccounts() != null)
            return false;
        if (getDebitCreditMap() != null ? !getDebitCreditMap().equals(that.getDebitCreditMap()) : that.getDebitCreditMap() != null)
            return false;
        if (getBeginningBalanceMap() != null ? !getBeginningBalanceMap().equals(that.getBeginningBalanceMap()) : that.getBeginningBalanceMap() != null)
            return false;
        if (getForeignBeginningBalanceMap() != null ? !getForeignBeginningBalanceMap().equals(that.getForeignBeginningBalanceMap()) : that.getForeignBeginningBalanceMap() != null)
            return false;
        if (getCurrencyId() != null ? !getCurrencyId().equals(that.getCurrencyId()) : that.getCurrencyId() != null)
            return false;
        if (getBaseCurrencyId() != null ? !getBaseCurrencyId().equals(that.getBaseCurrencyId()) : that.getBaseCurrencyId() != null)
            return false;
        if (getExchangeRateScale() != null ? !getExchangeRateScale().equals(that.getExchangeRateScale()) : that.getExchangeRateScale() != null)
            return false;
        if (getPrevExchangeRate() != null ? !getPrevExchangeRate().equals(that.getPrevExchangeRate()) : that.getPrevExchangeRate() != null)
            return false;
        if (getExchangeRate() != null ? !getExchangeRate().equals(that.getExchangeRate()) : that.getExchangeRate() != null)
            return false;
        if (getPrevBalanceDate() != null ? !getPrevBalanceDate().equals(that.getPrevBalanceDate()) : that.getPrevBalanceDate() != null)
            return false;
        if (getToDate() != null ? !getToDate().equals(that.getToDate()) : that.getToDate() != null) return false;
        if (getForeignDebitCreditMap() != null ? !getForeignDebitCreditMap().equals(that.getForeignDebitCreditMap()) : that.getForeignDebitCreditMap() != null)
            return false;
        if (getGainLoss() != null ? !getGainLoss().equals(that.getGainLoss()) : that.getGainLoss() != null)
            return false;
        if (getShowAccounts() != null ? !getShowAccounts().equals(that.getShowAccounts()) : that.getShowAccounts() != null)
            return false;
        if (getMapAccountTypeByList() != null ? !getMapAccountTypeByList().equals(that.getMapAccountTypeByList()) : that.getMapAccountTypeByList() != null)
            return false;
        if (getReTotalDebitCredit() != null ? !getReTotalDebitCredit().equals(that.getReTotalDebitCredit()) : that.getReTotalDebitCredit() != null)
            return false;
        if (getTrailBalanceTotalMap() != null ? !getTrailBalanceTotalMap().equals(that.getTrailBalanceTotalMap()) : that.getTrailBalanceTotalMap() != null)
            return false;
        if (getTempAccountByTypeMapSummary() != null ? !getTempAccountByTypeMapSummary().equals(that.getTempAccountByTypeMapSummary()) : that.getTempAccountByTypeMapSummary() != null)
            return false;
        if (getTempAccountByTypeMap() != null ? !getTempAccountByTypeMap().equals(that.getTempAccountByTypeMap()) : that.getTempAccountByTypeMap() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAccount() != null ? getAccount().hashCode() : 0;
        result = 31 * result + (isSummary() ? 1 : 0);
        result = 31 * result + (getAccounts() != null ? getAccounts().hashCode() : 0);
        result = 31 * result + (getDebitCreditMap() != null ? getDebitCreditMap().hashCode() : 0);
        result = 31 * result + (getBeginningBalanceMap() != null ? getBeginningBalanceMap().hashCode() : 0);
        result = 31 * result + (getForeignBeginningBalanceMap() != null ? getForeignBeginningBalanceMap().hashCode() : 0);
        result = 31 * result + (getCurrencyId() != null ? getCurrencyId().hashCode() : 0);
        result = 31 * result + (getBaseCurrencyId() != null ? getBaseCurrencyId().hashCode() : 0);
        result = 31 * result + (getExchangeRateScale() != null ? getExchangeRateScale().hashCode() : 0);
        result = 31 * result + (getPrevExchangeRate() != null ? getPrevExchangeRate().hashCode() : 0);
        result = 31 * result + (getExchangeRate() != null ? getExchangeRate().hashCode() : 0);
        result = 31 * result + (getPrevBalanceDate() != null ? getPrevBalanceDate().hashCode() : 0);
        result = 31 * result + (getToDate() != null ? getToDate().hashCode() : 0);
        result = 31 * result + (getForeignDebitCreditMap() != null ? getForeignDebitCreditMap().hashCode() : 0);
        result = 31 * result + (getGainLoss() != null ? getGainLoss().hashCode() : 0);
        result = 31 * result + (getShowAccounts() != null ? getShowAccounts().hashCode() : 0);
        result = 31 * result + (getMapAccountTypeByList() != null ? getMapAccountTypeByList().hashCode() : 0);
        result = 31 * result + (getReTotalDebitCredit() != null ? getReTotalDebitCredit().hashCode() : 0);
        result = 31 * result + (getTrailBalanceTotalMap() != null ? getTrailBalanceTotalMap().hashCode() : 0);
        result = 31 * result + (getTempAccountByTypeMapSummary() != null ? getTempAccountByTypeMapSummary().hashCode() : 0);
        result = 31 * result + (getTempAccountByTypeMap() != null ? getTempAccountByTypeMap().hashCode() : 0);
        result = 31 * result + (isParent() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrialBalanceAccountsItem{" +
                "account=" + account +
                ", summary=" + summary +
                ", accounts=" + accounts +
                ", debitCreditMap=" + debitCreditMap +
                ", beginningBalanceMap=" + beginningBalanceMap +
                ", foreignBeginningBalanceMap=" + foreignBeginningBalanceMap +
                ", currencyId=" + currencyId +
                ", baseCurrencyId=" + baseCurrencyId +
                ", exchangeRateScale=" + exchangeRateScale +
                ", prevExchangeRate=" + prevExchangeRate +
                ", exchangeRate=" + exchangeRate +
                ", prevBalanceDate=" + prevBalanceDate +
                ", toDate=" + toDate +
                ", foreignDebitCreditMap=" + foreignDebitCreditMap +
                ", gainLoss=" + gainLoss +
                ", showAccounts=" + showAccounts +
                ", mapAccountTypeByList=" + mapAccountTypeByList +
                ", reTotalDebitCredit=" + reTotalDebitCredit +
                ", trailBalanceTotalMap=" + trailBalanceTotalMap +
                ", tempAccountByTypeMapSummary=" + tempAccountByTypeMapSummary +
                ", tempAccountByTypeMap=" + tempAccountByTypeMap +
                ", parent=" + parent +
                '}';
    }
}
