package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/18/11
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalance implements IsSerializable {
    private SelectItem crmAccountItem;
    private ArrayList<CrmAccountCurrencyBalance> currencyBalances;
    private Boolean multipleCurrencyReport;
    private String clientCompanyName;
    private Integer totalCount;

    public SelectItem getCrmAccountItem() {
        return crmAccountItem;
    }

    public void setCrmAccountItem(SelectItem crmAccountItem) {
        this.crmAccountItem = crmAccountItem;
    }

    public ArrayList<CrmAccountCurrencyBalance> getCurrencyBalances() {
        return currencyBalances;
    }

    public void setCurrencyBalances(ArrayList<CrmAccountCurrencyBalance> currencyBalances) {
        this.currencyBalances = currencyBalances;
    }

    public Boolean isMultipleCurrencyReport() {
        return multipleCurrencyReport != null ? multipleCurrencyReport : false;
    }

    public void setMultipleCurrencyReport(Boolean multipleCurrencyReport) {
        this.multipleCurrencyReport = multipleCurrencyReport;
    }

    public Integer getTotalCount() {
        return totalCount != null ? totalCount : 0;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
