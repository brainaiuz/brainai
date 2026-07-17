package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/20/12
 * Time: 7:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManualTransactionData implements IsSerializable {

    private NewManualTransaction manualTransactionItem;
    private CurrencyItem[] currencyItems;
    private CurrencyItem baseCurrency;
    private String layoutHtml;
    private Date conversionDate;
    private Integer currentUserId;
    private HistoryListItem[] historyListItems;
    private boolean isApprover;
    private boolean isApproverSaved;
    private boolean isSetUpAP; //Approval Process

    public ManualTransactionData() {

    }


    public NewManualTransaction getManualTransactionItem() {
        return manualTransactionItem;
    }

    public void setManualTransactionItem(NewManualTransaction manualTransactionItem) {
        this.manualTransactionItem = manualTransactionItem;
    }

    public CurrencyItem[] getCurrencyItems() {
        return currencyItems;
    }

    public void setCurrencyItems(CurrencyItem[] currencyItems) {
        this.currencyItems = currencyItems;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public HistoryListItem[] getHistoryListItems() {
        return historyListItems;
    }

    public void setHistoryListItems(HistoryListItem[] historyListItems) {
        this.historyListItems = historyListItems;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public boolean isApproverSaved() {
        return isApproverSaved;
    }

    public void setApproverSaved(boolean approverSaved) {
        isApproverSaved = approverSaved;
    }

    public boolean isSetUpAP() {
        return isSetUpAP;
    }

    public void setSetUpAP(boolean setUpAP) {
        isSetUpAP = setUpAP;
    }
}
