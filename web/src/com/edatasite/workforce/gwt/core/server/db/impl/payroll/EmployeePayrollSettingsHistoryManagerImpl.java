package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EmployeePayrollSettingsHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 2:07:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeePayrollSettingsHistoryManager")
public class EmployeePayrollSettingsHistoryManagerImpl extends BaseManager<EmployeePayrollSettingsHistory> implements EmployeePayrollSettingsHistoryManager {

    public EmployeePayrollSettingsHistoryManagerImpl() {
        super(EmployeePayrollSettingsHistory.class);
    }

    public List<EmployeePayrollSettingsHistory> getCompanyEmployeePayrollSettingsHistory(ListingFilterParameter fp) {
        String query = "from EmployeePayrollSettingsHistory ";
        if (fp != null && fp.getType() != null) {
            query += " where status.code='" + (fp.getType().equals(1) ? EmployeePayrollSettingsHistory.NICATEGORY_CHANGED : EmployeePayrollSettingsHistory.TAXCODE_CHANGED) + "'";
        }
        query += " order by date desc";
        return find(query);
    }

    public List<EmployeePayrollSettingsHistory> getNiTaxCodeChangesByPeriod(ListingFilterParameter fp, Date startDate, Date endDate) {
        Map params = new HashMap();
        params.put("employeeId", fp.getEmployeeId());
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (fp != null && fp.getType() != null) {
            if (1 == fp.getType()) {
                params.put("type", EmployeePayrollSettingsHistory.NICATEGORY_CHANGED);
            } else if (2 == fp.getType()) {
                params.put("type", EmployeePayrollSettingsHistory.TAXCODE_CHANGED);
            }
        }
        String query = "from EmployeePayrollSettingsHistory where employeePayrollSettings.employee.objectID=:employeeId and status.code=:type" +
                " and date between :startDate and :endDate order by date desc";
        return findByNamedParams(query, params);
    }
}
