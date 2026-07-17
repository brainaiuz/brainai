package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsMultiCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 25/10/12
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public interface MultiCurrencyManager extends Manager<EdsMultiCurrency> {

    List<SelectItem> getSubsidiariesCompanyCurrencies(EdsCurrency edsCurrency);

    List<EdsCurrency> getAllCompanyCurrencies();

    List<EdsCurrency> getCompanyCurrencyList();

    Map<Integer,List<Integer>> getCompaniesCurrencyMap();

    List<EdsCurrency> getSubsidiaryCurrencies();

    HashMap<Integer,Integer> getSubsidiaryCurrenciesAsMap();
}
