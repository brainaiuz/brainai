package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 10/11/12
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class MultiCurrencyExchangeRateItem implements IsSerializable {

    private Integer companySignUpMonth;
    private Integer companySignUpYear;
    private SelectItem parentBaseCurrency;
    private ArrayList<SelectItem> subsidiariesMultiCurrencyList;
    private LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> exhangeRateMap;// first key currencyId { second key month of day }

    public Integer getCompanySignUpMonth() {
        return companySignUpMonth;
    }

    public void setCompanySignUpMonth(Integer companySignUpMonth) {
        this.companySignUpMonth = companySignUpMonth;
    }

    public Integer getCompanySignUpYear() {
        return companySignUpYear;
    }

    public void setCompanySignUpYear(Integer companySignUpYear) {
        this.companySignUpYear = companySignUpYear;
    }

    public SelectItem getParentBaseCurrency() {
        return parentBaseCurrency;
    }

    public void setParentBaseCurrency(SelectItem parentBaseCurrency) {
        this.parentBaseCurrency = parentBaseCurrency;
    }

    public ArrayList<SelectItem> getSubsidiariesMultiCurrencyList() {
        if (subsidiariesMultiCurrencyList == null) {
            subsidiariesMultiCurrencyList = new ArrayList<>();
        }
        return subsidiariesMultiCurrencyList;
    }

    public void setSubsidiariesMultiCurrencyList(ArrayList<SelectItem> subsidiariesMultiCurrencyList) {
        this.subsidiariesMultiCurrencyList = subsidiariesMultiCurrencyList;
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> getExhangeRateMap() {
        if (exhangeRateMap == null) {
            exhangeRateMap = new LinkedHashMap<>();
        }
        return exhangeRateMap;
    }

    public void setExhangeRateMap(LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> exhangeRateMap) {
        this.exhangeRateMap = exhangeRateMap;
    }
}
