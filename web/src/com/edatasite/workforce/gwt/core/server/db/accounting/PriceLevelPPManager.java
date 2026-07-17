package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelPP;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 7:30:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PriceLevelPPManager extends Manager<EdsPriceLevelPP> {

    void deletePriceLevelPPByPL(Integer objectID);

    void deletePriceLevelPPByProduct(Integer productID);

    EdsPriceLevelPP getByPriceLevelIdAndProductId(Integer priceLevelId, Integer productId);

    List<EdsPriceLevelPP> getByPriceLevelsByProductId(Integer productId);

    List<EdsPriceLevelPP> getItemsByPriceLevelId(Integer priceLevelId, String searchKey);

    Integer getTotalCount(Integer priceLevelId);

    Map<Integer, HashMap<PriceLevelItem, PriceLevelPPItem>> getPriceLevelPPItemsByIds(Set<Integer> itemIds);
}
