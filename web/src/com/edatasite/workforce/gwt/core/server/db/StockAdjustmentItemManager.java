package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;

import java.util.List;

/**
 * Created by Sherzod on 3/15/2016.
 */
public interface StockAdjustmentItemManager extends Manager<EdsAdjustmentItem> {

    List<ProjectBudgetItem> getStockAdjustmentItems(ListingFilterParameter fp);

    boolean isUsedInStockAdjustment(Integer productId);
}
