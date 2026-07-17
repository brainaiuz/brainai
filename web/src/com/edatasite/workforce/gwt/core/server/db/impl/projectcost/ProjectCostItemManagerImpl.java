package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsProjectCostItem;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.ProjectCostItemManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Date: 15-May-2010
 * Time: 14:10:14
 */
@Repository("projectCostItemManager")
public class ProjectCostItemManagerImpl extends BaseManager<EdsProjectCostItem> implements ProjectCostItemManager {
    public ProjectCostItemManagerImpl() {
        super(EdsProjectCostItem.class);
    }
}
