package com.edatasite.workforce.gwt.employee.server.app;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 16:04:46
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollSettings;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.DepartmentDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface EmployeeServiceLocal {

    PayrollSettings getEmployeeDetailsAndPayrollSettings(Integer employeeId, Date date);

    void createAssignUsersToGroups(EdsUser user);

    boolean setEmployeeLocation(Integer employeeID, Double latitude, Double longitude);

    Integer createEmployeeInternal(NewEmployee employee, Integer parentEmployeeId);

    Integer checkUserLimit(boolean essUser, boolean hasAccess, Integer companyID);

    NumberData generateEmployeeNumber(Integer objectID);

    Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess, boolean indexSolr);

    Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess, boolean indexSolr, Boolean isEss);

    void activateOrDisactivateEmployee(Integer employeeID, Boolean grantAccess, boolean indexSolr);

    EmployeeViewItem getEmployeeByDriverNumber(Long driverNumber);

    EmployeeViewItem getCurrentEmployee();

    Integer getEmployeeIdByDriverNumber(Long driverNumber);

    Integer createPosition(NewPosition position);

    Integer[] getAllEmployeesMaxCount(Integer companyID, Integer exceptEmployee);

    ListResult<EmployeeListItem> getEmployeeList(ListingFilterParameter filterParametrs);

    List<DepartmentDto> getDepartmentEmployees(String searchText, Integer start, Integer limit);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getProjectEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole);

    void updateEmployeeSocialData(Integer employeeID, String socialImageUrl);

    SelectItem[] getProjectEmployeesAsSelectItem(ListingFilterParameter fp);

    Integer[] createEmployees(NewEmployee[] employees);

    Long[] getEmployeesGenderRatio();

    SelectItem[] getCompanyEmployeesAsSelectItems(ListingFilterParameter fp);

    ListResult<EmployeeListItem> getEmployeesListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery);

    HashMap<String, String> getEmployeePayrollSettings(Integer employeeID);
}
