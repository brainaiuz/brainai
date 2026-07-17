package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsEstimateOfContractCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.EstimateOfContractCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Ilhom
 * Date: 01.07.13
 * Time: 17:04
 */
@Repository("estimateOfContractCustomFieldsManager")
public class EstimateOfContractCustomFieldsManagerImpl extends BaseManager<EdsEstimateOfContractCustomFields> implements EstimateOfContractCustomFieldsManager {

    public EstimateOfContractCustomFieldsManagerImpl() {
        super(EdsEstimateOfContractCustomFields.class);
    }
}