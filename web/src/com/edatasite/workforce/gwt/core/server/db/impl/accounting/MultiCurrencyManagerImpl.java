package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsMultiCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.accounting.MultiCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 25/10/12
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
@Repository("multiCurrencyManager")
public class MultiCurrencyManagerImpl extends BaseManager<EdsMultiCurrency> implements MultiCurrencyManager {
    public MultiCurrencyManagerImpl() {
        super(EdsMultiCurrency.class);
    }

    @Override
    public List<SelectItem> getSubsidiariesCompanyCurrencies(EdsCurrency edsCurrency) {
        return find("SELECT NEW com.edatasite.workforce.gwt.core.client.rpc.SelectItem(currency.objectID,currency.name,currency.fullName) " +
                " FROM EdsMultiCurrency multicurrency INNER JOIN multicurrency.currency currency " +
                " WHERE currency.objectID<>?" +
                " GROUP BY currency.objectID,currency.name,currency.fullName ", edsCurrency.getObjectID());
    }

    @Override
    public List<EdsCurrency> getAllCompanyCurrencies() {
        return find("SELECT DISTINCT currency FROM EdsMultiCurrency multicurrency " +
                " INNER JOIN multicurrency.currency currency ");
    }

    @Override
    public List<EdsCurrency> getCompanyCurrencyList() {
        return find("SELECT multicurrency.currency FROM EdsMultiCurrency multicurrency WHERE multicurrency.companyId IS NULL");
    }

    @Override
    public Map<Integer, List<Integer>> getCompaniesCurrencyMap() {

        List<Object[]> companiesCurrencyList = find("SELECT multicurrency.companyId,multicurrency.currency.objectID FROM EdsMultiCurrency multicurrency " +
                " WHERE multicurrency.companyId IS NOT NULL" +
                " GROUP BY multicurrency.companyId,multicurrency.currency.objectID ");

        Map<Integer, List<Integer>> companiesCurrencyMap = new HashMap<>();
        for (Object[] objects : companiesCurrencyList) {
            Integer companyId = (Integer) objects[0];
            Integer currencyId = (Integer) objects[1];
            if (!companiesCurrencyMap.containsKey(companyId)) {
                companiesCurrencyMap.put(companyId, new ArrayList<>());
            }
            companiesCurrencyMap.get(companyId).add(currencyId);
        }
        return companiesCurrencyMap;
    }

    @Override
    public List<EdsCurrency> getSubsidiaryCurrencies() {
        return find("select distinct mc.currency from EdsMultiCurrency mc where mc.currency is not null");
    }

    @Override
    public HashMap<Integer, Integer> getSubsidiaryCurrenciesAsMap() {
        HashMap<Integer, Integer> currencyMap = new HashMap<>();
        List<EdsCurrency> currencies = getSubsidiaryCurrencies();
        for (EdsCurrency c : currencies) {
            currencyMap.put(c.getObjectID(), c.getObjectID());
        }
        return currencyMap;
    }
}
