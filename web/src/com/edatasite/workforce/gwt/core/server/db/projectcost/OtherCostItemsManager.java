package com.edatasite.workforce.gwt.core.server.db.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsOtherCostItems;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Dilsh0d
 * Date: 13-May-2010
 * Time: 16:22:59
 */
public interface OtherCostItemsManager extends Manager<EdsOtherCostItems> {
    List<EdsOtherCostItems> list(Integer resourceTypeId);
}
