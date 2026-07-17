package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsPositionCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.PositionCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Jamshid on 11/01/2022
 */

@Repository("positionCFManager")
public class PositionCFManagerImpl extends BaseManager<EdsPositionCustomFields> implements PositionCFManager {
    public PositionCFManagerImpl() {
        super(EdsPositionCustomFields.class);
    }
}
