package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAssemblyItemItems;
import com.edatasite.workforce.gwt.core.server.db.AssemblyItemItemsManager;
import org.springframework.stereotype.Repository;

@Repository("assemblyItemItemsManager")
public class AssemblyItemItemsManagerImp extends BaseManager<EdsAssemblyItemItems> implements AssemblyItemItemsManager {
    public AssemblyItemItemsManagerImp() {
        super(EdsAssemblyItemItems.class);
    }
}
