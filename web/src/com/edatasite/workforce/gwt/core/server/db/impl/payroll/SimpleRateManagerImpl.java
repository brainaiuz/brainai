package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipEmployeeBonus;
import com.edatasite.workforce.core.domain.payrolluk.EdsSimpleRate;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SimpleRateManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-19
 * Time: 8:04 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("simpleRateManager")
public class SimpleRateManagerImpl extends BaseManager<EdsSimpleRate> implements SimpleRateManager {

    @Override
    public EdsPayslipEmployeeBonus getEmployeeBonus(Integer employeeID) {
        return (EdsPayslipEmployeeBonus)findSingle("SELECT peb FROM EdsPayslipEmployeeBonus peb WHERE peb.employee.objectID = ? ORDER BY peb.id DESC", employeeID);
    }

    public SimpleRateManagerImpl() {
        super(EdsSimpleRate.class);
    }
}
