package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsEmployeeStepCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeStepCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:23:19
 */
@Repository("employeeStepCFManager")
public class EmployeeStepCFManagerImpl extends BaseManager<EdsEmployeeStepCustomFields> implements EmployeeStepCFManager {
    public EmployeeStepCFManagerImpl() {
        super(EdsEmployeeStepCustomFields.class);
    }
}
