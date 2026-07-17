package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;
import java.util.Map;

public interface CurrencyManager extends Manager<EdsCurrency> {

    String ID = "objectID";
    String NAME = "name";
    String USD = "USD";
    String EUR = "EUR";
    String GBP = "GBP";

    EdsCurrency getCurrency(Integer objectID);

    EdsCurrency getCurrency(String name);

    List<EdsCurrency> getAllCurrency();

    List<EdsCurrency> getAllCurrency(ListingFilterParameter fp);

    List<EdsCurrency> getStandartCurrency();

    Map<String, EdsCurrency> getListAsMap();

}
