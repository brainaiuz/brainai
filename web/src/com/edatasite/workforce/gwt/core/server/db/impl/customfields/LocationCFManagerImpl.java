package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsLocationCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.LocationCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Jamshid on 24/01/2022
 */

@Repository("locationCFManager")
public class LocationCFManagerImpl extends BaseManager<EdsLocationCustomFields> implements LocationCFManager {
    public LocationCFManagerImpl() {
        super(EdsLocationCustomFields.class);
    }
}
