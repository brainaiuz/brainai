package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.BenefitRequestCFManager;
import com.edatasite.workforce.core.domain.customfields.EdsBenefitRequestCustomFields;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("benefitRequestCFManager")
public class BenefitRequestCFManagerIml extends BaseManager<EdsBenefitRequestCustomFields> implements BenefitRequestCFManager {
    public BenefitRequestCFManagerIml() {
        super(EdsBenefitRequestCustomFields.class);
    }
}
