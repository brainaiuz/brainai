package com.edatasite.workforce.gwt.core.server.db.impl.customfields;


import com.edatasite.workforce.core.domain.customfields.EdsContractCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ContractCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Omonullo Abdullaev on 8/4/2016.
 */
@Repository("contractCFManager")
public class ContractCFManagerImpl extends BaseManager<EdsContractCustomFields> implements ContractCFManager {
    public ContractCFManagerImpl() {
        super(EdsContractCustomFields.class);
    }
}
