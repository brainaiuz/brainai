package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsAdditionalPaymentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.AdditionalPaymentCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("additionalPaymentCFManager")
public class AdditionalPaymentCFManagerImpl extends BaseManager<EdsAdditionalPaymentCustomFields> implements AdditionalPaymentCFManager {

    public AdditionalPaymentCFManagerImpl() {
        super(EdsAdditionalPaymentCustomFields.class);
    }
}
