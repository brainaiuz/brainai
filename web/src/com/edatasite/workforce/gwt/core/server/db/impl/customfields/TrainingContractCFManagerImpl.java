package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsTrainingContractCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.TrainingContractCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("trainingContractCFManager")
public class TrainingContractCFManagerImpl  extends BaseManager<EdsTrainingContractCustomFields> implements TrainingContractCFManager {
    public TrainingContractCFManagerImpl() {
        super(EdsTrainingContractCustomFields.class);
    }
}

