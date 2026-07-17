package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsRFPCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRFQCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.RFPCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RFQCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("rfpCFManager")
public class RFPCFManagerImpl extends BaseManager<EdsRFPCustomFields> implements RFPCFManager {

    public RFPCFManagerImpl() {
        super(EdsRFPCustomFields.class);
    }
}
