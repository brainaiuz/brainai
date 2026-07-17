package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 24, 2010
 * Time: 5:53:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PickListManager extends Manager<EdsPickList> {

    EdsPickList getPickListBySaleQuoteID(Integer saleQuoteID);

    List<EdsPickList> list(ListingFilterParameter filterParametrs);

    void deletePickListAndItemsByQuote(Integer saleQuoteID);

    Integer listCount(ListingFilterParameter filterParametrs);

    List<EdsPickList> getPickListBySaleQuoteIDs(String ids);
}
