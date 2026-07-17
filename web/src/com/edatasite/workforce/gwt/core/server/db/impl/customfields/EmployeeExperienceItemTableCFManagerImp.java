package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeExperienceItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("employeeExperienceItemTableCFManager")
public class EmployeeExperienceItemTableCFManagerImp  extends BaseManager<EdsEmployeeExperienceItemTableCF> implements EmployeeExperienceItemTableCFManager {

    public EmployeeExperienceItemTableCFManagerImp() {
        super(EdsEmployeeExperienceItemTableCF.class);
    }
}
