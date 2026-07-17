package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObjectData;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeObjectDataManager;
import org.springframework.stereotype.Repository;

@Repository("overtimeObjectDataMangerImpl")
public class OvertimeObjectDataMangerImpl extends BaseManager<EdsOvertimeObjectData> implements OvertimeObjectDataManager {
    public OvertimeObjectDataMangerImpl() {
        super(EdsOvertimeObjectData.class);
    }
}
