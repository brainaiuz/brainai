package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.PlacementItemTableCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class PlacementItemTableCFManagerImpl extends BaseManager<EdsPlacementItemTableCF> implements PlacementItemTableCFManager {
    public PlacementItemTableCFManagerImpl() {
        super(EdsPlacementItemTableCF.class);
    }
}
