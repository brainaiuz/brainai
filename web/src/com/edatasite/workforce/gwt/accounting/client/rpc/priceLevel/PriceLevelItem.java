package com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel;

import com.edatasite.workforce.gwt.accounting.client.rpc.enums.PriceLevelOperationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.HasObjectPermission;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:33:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelItem extends HasObjectPermission {
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String PLCASE = "plcase";
    public static final String ACTION = "action";


    private Integer type;
    private Integer plCase;
    private Double percent;
    private PriceLevelPPItem[] priceLevelPPItems;
    private PriceLevelBBItem[] priceLevelBBItems;

    private SelectItem[] appliedClients;
    private SelectItem[] appliedClientTypes;

    private CurrencyItem baseCurrency;
    private CurrencyItem currency;

    private CurrencyItem[] currencyList;
    private SelectItem[] clientTypeList;

    private String qbPriceLevelId;
    private String qbEditSequence;
    private boolean fromQuickbooks;

    private Integer totalCountPerProductItems = 0;

    private PriceLevelOperationTypeEnum operationType;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPLCase() {
        return plCase;
    }

    public void setPLCase(Integer plCase) {
        this.plCase = plCase;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public SelectItem[] getAppliedClients() {
        return appliedClients;
    }

    public void setAppliedClients(SelectItem[] appliedClients) {
        this.appliedClients = appliedClients;
    }


    public SelectItem[] getAppliedClientTypes() {
        return appliedClientTypes;
    }

    public void setAppliedClientTypes(SelectItem[] appliedClientTypes) {
        this.appliedClientTypes = appliedClientTypes;
    }

    public PriceLevelPPItem[] getPriceLevelPPItems() {
        return priceLevelPPItems;
    }

    public void setPriceLevelPPItems(PriceLevelPPItem[] priceLevelPPItems) {
        this.priceLevelPPItems = priceLevelPPItems;
    }

    public PriceLevelBBItem[] getPriceLevelBBItems() {
        return priceLevelBBItems;
    }

    public void setPriceLevelBBItems(PriceLevelBBItem[] priceLevelBBItems) {
        this.priceLevelBBItems = priceLevelBBItems;
    }

    public CurrencyItem[] getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(CurrencyItem[] currencyList) {
        this.currencyList = currencyList;
    }

    public SelectItem[] getClientTypeList() {
        return clientTypeList;
    }

    public void setClientTypeList(SelectItem[] clientTypeList) {
        this.clientTypeList = clientTypeList;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQbPriceLevelId() {
        return qbPriceLevelId;
    }

    public void setQbPriceLevelId(String qbPriceLevelId) {
        this.qbPriceLevelId = qbPriceLevelId;
    }

    public String getQbEditSequence() {
        return qbEditSequence;
    }

    public void setQbEditSequence(String qbEditSequence) {
        this.qbEditSequence = qbEditSequence;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public PriceLevelOperationTypeEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(PriceLevelOperationTypeEnum operationType) {
        this.operationType = operationType;
    }

    public Integer getTotalCountPerProductItems() {
        return totalCountPerProductItems;
    }

    public void setTotalCountPerProductItems(Integer totalCountPerProductItems) {
        this.totalCountPerProductItems = totalCountPerProductItems;
    }
}
