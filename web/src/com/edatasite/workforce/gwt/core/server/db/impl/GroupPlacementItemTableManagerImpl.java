package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacementItemTable;
import com.edatasite.workforce.gwt.core.server.db.GroupPlacementItemTableManager;
import org.springframework.stereotype.Repository;

@Repository("groupPlacementItemTableManager")
public class GroupPlacementItemTableManagerImpl extends BaseManager<EdsGroupPlacementItemTable> implements GroupPlacementItemTableManager {
    public GroupPlacementItemTableManagerImpl() {
        super(EdsGroupPlacementItemTable.class);
    }

}
