package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StockAdjustmentManager extends Manager<EdsStockAdjustment> {
    List<EdsStockAdjustment> getList(ListingFilterParameter filterParametrs);

    Integer getTotalCount(ListingFilterParameter fp);

    List<EdsStockAdjustment> getStockAdjustmentsByStockTransfer(Integer objectID);

    List<EdsStockAdjustment> getStockAdjustmentsByStockTransfer(Integer objectID, boolean evenDeleted);

    Integer getStockAdjustmentIntNumber();

    boolean numberExists(String numberString, Integer objectId);

    EdsStockAdjustment getByNumber(String number);

}
