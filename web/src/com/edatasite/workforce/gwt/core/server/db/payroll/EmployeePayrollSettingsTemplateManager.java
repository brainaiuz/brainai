package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettingsTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/25/15
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeePayrollSettingsTemplateManager extends Manager<EdsEmployeePayrollSettingsTemplate> {

    List<EdsEmployeePayrollSettingsTemplate> getEmployeeTemplateList(ListingFilterParameter lfp);

    Integer getEmployeeTemplateCount();

    EdsEmployeePayrollSettingsTemplate getEmployeeAssignedTemplate(Integer employeeId);

    HashMap<Integer, String> getEmployeeAssignedTemplateMap(String employeeIds);

}
