package com.edatasite.workforce.gwt.core.server.rabbitmq.data;


import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 03/11/12
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class MultiCurrencyItemMQ implements Serializable {

    private Integer currencyRegCompanyId;
    private List<Integer> currencyIds;

    public MultiCurrencyItemMQ(Integer companyId, List<Integer> currencyIds) {
        this.currencyRegCompanyId = companyId;
        this.currencyIds = currencyIds;
    }

    public Integer getCurrencyRegCompanyId() {
        return currencyRegCompanyId;
    }

    public void setCurrencyRegCompanyId(Integer currencyRegCompanyId) {
        this.currencyRegCompanyId = currencyRegCompanyId;
    }

    public List<Integer> getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(List<Integer> currencyIds) {
        this.currencyIds = currencyIds;
    }
}
