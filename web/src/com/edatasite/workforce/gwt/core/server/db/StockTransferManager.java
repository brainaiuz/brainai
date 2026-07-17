package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 2/25/15.
 */
public interface StockTransferManager extends Manager<EdsStockTransfer> {

    ArrayList<EdsStockTransfer> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    Integer getStockTranferNumberInt();

    boolean numberExists(String numberString, Integer objectId);

}
