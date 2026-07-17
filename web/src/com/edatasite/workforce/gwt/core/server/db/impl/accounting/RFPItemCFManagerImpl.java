package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsRFPItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("rfpItemCFManager")
public class RFPItemCFManagerImpl extends BaseManager<EdsRFPItemCustomFields> implements RFPItemCFManager {

    public RFPItemCFManagerImpl() {
        super(EdsRFPItemCustomFields.class);
    }
}