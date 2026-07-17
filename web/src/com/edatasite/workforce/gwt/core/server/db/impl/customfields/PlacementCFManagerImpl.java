package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsPlacementCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.PLacementCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Hurshid on 8/24/2018.
 */
@Repository
public class PlacementCFManagerImpl extends BaseManager<EdsPlacementCustomFields> implements PLacementCFManager {

    public PlacementCFManagerImpl() {
        super(EdsPlacementCustomFields.class);
    }
}
