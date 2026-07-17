package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMonthlyTimesheetPayment;

/**
 * Created by Normurod on 11/9/2016.
 */
public interface MonthlyTimesheetPaymentManager extends Manager<EdsMonthlyTimesheetPayment> {
    void deleteItemsByPayslip(Integer objectID);
}
