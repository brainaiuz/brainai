package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsEmployeeItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.EmployeeItemTableCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeItemTableCFManagerImpl extends BaseManager<EdsEmployeeItemTableCF> implements EmployeeItemTableCFManager {

    public EmployeeItemTableCFManagerImpl() {
        super(EdsEmployeeItemTableCF.class);
    }
}