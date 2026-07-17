package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsOtherCostItems;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.OtherCostItemsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d
 * Date: 13-May-2010
 * Time: 16:24:29
 */
@Repository("otherCostItemsManager")
public class OtherCostItemsManagerImpl extends BaseManager<EdsOtherCostItems> implements OtherCostItemsManager {
    public OtherCostItemsManagerImpl() {
        super(EdsOtherCostItems.class);
    }

    public List<EdsOtherCostItems> list(Integer resourceTypeId) {
        return find("SELECT other FROM EdsOtherCostItems other  WHERE other.resourceType.objectID=?", resourceTypeId);
    }
}
