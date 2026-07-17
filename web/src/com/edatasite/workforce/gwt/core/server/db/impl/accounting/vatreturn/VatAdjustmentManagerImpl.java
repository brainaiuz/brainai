package com.edatasite.workforce.gwt.core.server.db.impl.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatAdjustment;
import com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn.VatAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository
public class VatAdjustmentManagerImpl extends BaseManager<EdsVatAdjustment> implements VatAdjustmentManager {

    public VatAdjustmentManagerImpl() {
        super(EdsVatAdjustment.class);
    }
}
