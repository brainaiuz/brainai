package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsRFQItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("rfqItemCFManager")
public class RFQItemCFManagerImpl extends BaseManager<EdsRFQItemCustomFields> implements RFQItemCFManager {

    public RFQItemCFManagerImpl() {
        super(EdsRFQItemCustomFields.class);
    }
}
