package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:57:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PriceLevelManager extends Manager<EdsPriceLevel> {
//    List<EdsPriceLevel> list(ListingFilterParameter filterParametrs, ListLoadConfig config);
//
    Integer listCount(ListingFilterParameter filterParameter);

    List<EdsPriceLevel> list(ListingFilterParameter filterParameter);

    void deletePriceLevel(Integer objectID);

    List<EdsPriceLevel> getPriceLevelsByIds(String Ids);

    List<EdsPriceLevel> getPriceLevelsByNotExportedToQB(Integer limit);

    EdsPriceLevel getPriceLevelByName(String name);

    List<EdsPriceLevel> getPriceLevels(Integer currenceId, Integer clientId, boolean showHiddens);

    boolean isPricelLevelNameExists(String name, Integer id);

    List<EdsPriceLevel> getPriceLevelsByClientType(Integer clientTypeID, Integer currencyID, boolean showHiddens);

}
