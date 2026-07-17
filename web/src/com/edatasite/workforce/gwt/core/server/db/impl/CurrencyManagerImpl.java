package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository("currencyManager")
public class CurrencyManagerImpl extends BaseManager<EdsCurrency> implements CurrencyManager {

    public CurrencyManagerImpl() {
        super(EdsCurrency.class);
    }

    public EdsCurrency getCurrency(Integer objectID) {
        return (EdsCurrency) findSingle("select c from EdsCurrency c where c.objectID=?", objectID);
    }

    public EdsCurrency getCurrency(String name) {
        return (EdsCurrency) findSingle("select c from EdsCurrency c where c.name=?", name);
    }

    public List<EdsCurrency> getAllCurrency() {
        return (List<EdsCurrency>) find("select c from EdsCurrency c where c.name is not null order by " +
                "(case when c.name=? then 1 when c.name=? then 2 when c.name=? then 3 else 4 end), c.name", GBP, USD, EUR);
    }

    public List<EdsCurrency> getAllCurrency(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c from EdsCurrency c where c.name is not null ");
        if (fp != null && fp.isLookUp() && fp.getSqlSearchKey() != null) {
            sql.append(" and lower(c.name) like '" + fp.getSqlSearchKey() + "' ");
        }
        sql.append(" order by (case when c.name=? then 1 when c.name=? then 2 when c.name=? then 3 else 4 end), c.name");
        return (List<EdsCurrency>) find(sql.toString(), GBP, USD, EUR);
    }

    public List<EdsCurrency> getStandartCurrency() {
        return find("select c from EdsCurrency c where c.name=? or c.name=? or c.name=?", USD, EUR, GBP);
    }

    @Override
    public Map<String, EdsCurrency> getListAsMap() {
        Map<String, EdsCurrency> map = new TreeMap<>();
        List<EdsCurrency> list = getAllCurrency();
        if (list != null && list.size() > 0) {
            for (EdsCurrency currency : list) {
                if (currency.getName() != null) {
                    map.put(currency.getName().toLowerCase().trim(), currency);
                }
                if (currency.getFullName() != null) {
                    map.put(currency.getFullName().toLowerCase().trim(), currency);
                }
                if (currency.getSymbol() != null) {
                    map.put(currency.getSymbol().toLowerCase().trim(), currency);
                }
            }
        }
        return map;
    }
}
