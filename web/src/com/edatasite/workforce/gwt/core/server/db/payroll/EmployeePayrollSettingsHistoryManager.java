package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EmployeePayrollSettingsHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 2:07:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeePayrollSettingsHistoryManager extends Manager<EmployeePayrollSettingsHistory> {
    List<EmployeePayrollSettingsHistory> getCompanyEmployeePayrollSettingsHistory(ListingFilterParameter fp);

    List<EmployeePayrollSettingsHistory> getNiTaxCodeChangesByPeriod(ListingFilterParameter fp, Date startDate, Date endDate);
}
