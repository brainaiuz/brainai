package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsRFQCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.RFQCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Abror Abdukadirov
 * Date: 04.11.2017 15:33
 */
@Repository("rfqCFManager")
public class RFQCFManagerImpl extends BaseManager<EdsRFQCustomFields> implements RFQCFManager {

    public RFQCFManagerImpl() {
        super(EdsRFQCustomFields.class);
    }
}
