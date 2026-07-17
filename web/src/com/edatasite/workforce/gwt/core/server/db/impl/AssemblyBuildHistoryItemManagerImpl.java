package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAssemblyBuildHistoryItem;
import com.edatasite.workforce.gwt.core.server.db.AssemblyBuildHistoryItemManager;
import org.springframework.stereotype.Repository;

@Repository("assemblyBuildHistoryItemManager")
public class AssemblyBuildHistoryItemManagerImpl extends BaseManager<EdsAssemblyBuildHistoryItem> implements AssemblyBuildHistoryItemManager {
    public AssemblyBuildHistoryItemManagerImpl() {
        super(EdsAssemblyBuildHistoryItem.class);
    }
}
