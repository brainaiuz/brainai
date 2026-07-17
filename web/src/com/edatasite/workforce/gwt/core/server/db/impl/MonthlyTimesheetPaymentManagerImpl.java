package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMonthlyTimesheetPayment;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetPaymentManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Normurod on 11/9/2016.
 */
@Repository("monthlyTimesheetPaymentManager")
public class MonthlyTimesheetPaymentManagerImpl extends BaseManager<EdsMonthlyTimesheetPayment> implements MonthlyTimesheetPaymentManager {

    public MonthlyTimesheetPaymentManagerImpl() {
        super(EdsMonthlyTimesheetPayment.class);
    }

    @Override
    public void deleteItemsByPayslip(Integer objectID) {
        updateNative("delete from " + getCompanyId() + ".monthly_timesheet_payment where payslip_id = " + objectID);
    }
}
