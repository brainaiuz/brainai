package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.customfields.EdsPayrollCustomFields;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCustomFieldsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Ilhom Lutfullaev on 20.10.2017.
 */

@Repository
public class PayrollCustomFieldsManagerImpl extends BaseManager<EdsPayrollCustomFields> implements PayrollCustomFieldsManager {
    public PayrollCustomFieldsManagerImpl() {
        super(EdsPayrollCustomFields.class);
    }
}
