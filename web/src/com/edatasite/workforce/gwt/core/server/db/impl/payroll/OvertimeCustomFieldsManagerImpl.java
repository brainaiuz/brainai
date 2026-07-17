package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.customfields.EdsOvertimeCustomFields;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeCustomFieldsManager;
import org.springframework.stereotype.Repository;

@Repository("overtimeCustomFieldsManager")
public class OvertimeCustomFieldsManagerImpl extends BaseManager<EdsOvertimeCustomFields> implements OvertimeCustomFieldsManager {
    public OvertimeCustomFieldsManagerImpl() {
        super(EdsOvertimeCustomFields.class);
    }
}
