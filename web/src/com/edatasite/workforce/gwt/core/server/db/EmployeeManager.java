package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;

import java.time.LocalDate;
import java.util.*;


/**
 * User: Admin
 * Date: 07.01.2008
 * Time: 17:10:31
 */
public interface EmployeeManager extends Manager<EdsEmployee> {

    Long getEmployeesCountByCompany(Boolean active, Boolean withNoAccess, boolean withoutEss);

    Long getNoAccessEmployeesCountByCompany();

    Long getEssUsersCount(Boolean active);

    Long getEmployeesCountByDepartment(EdsDepartment department);

    Long getEmployeesCountByHireDate(ListingFilterParameter fp);

    List<EdsEmployee> getEmployeeByRole(Integer roleID);

    List<EdsEmployee> getDirectors();

    List<EdsEmployee> getCompanyOtherAdmins(Integer employeeId);

    List<EdsEmployee> getEmployeeByRoleCode(String roleCode);

    Long getEmployeeCountByRoleCodeExceptByUserId(String roleCode, Integer userId);

    List<EdsUser> getUserByRole(Integer roleID);

    List<Integer> getEmployeesByApprover(EdsApprover approver, ListingFilterParameter filterParametrs);

    List<EdsEmployee> getDirectors2();

    ListingObjectItem<Object[]> getTeamOrAllEmployees(ListingFilterParameter filterParameter);

    List<EdsEmployee> getEmployees(EdsCompany company);

    List<EdsEmployee> getEmployeesForPayroll(EdsCompany company);

    List<EdsEmployee> getActiveEmployees(EdsCompany company);

    List<EdsEmployee> getEmployeeSortName(EdsCompany company);

    List<Integer> getEmployeeIds();

    List<EdsEmployee> getEmployeesByIds(String Ids);

    List<EdsEmployee> getEmployeesByPermissionCode(String permissionCode);

    List<EdsEmployee> getOwnersByPermission(String permissionCode);

    List<EdsEmployee> getAssignees(ListingFilterParameter filterParameter);

    Integer getAssigneesTotalCount(ListingFilterParameter filterParameter);

    Map<String, Integer> getEmployeesByPermissionCodeAsMap(String permissionCode);

    List<EdsEmployee> getHRManagers();

    List<EdsEmployee> getAdministrators();

    List<EdsEmployee> list(ListingFilterParameter fp, boolean fromRU);

    List<EdsEmployee> empList(Integer companyId);

    List<EdsEmployee> list(ListingFilterParameter fp);

    List<EdsEmployee> empListForEOS(ListingFilterParameter fp);

    List<EmployeeListItem> getEmployeeList();

    List<EmployeeListItem> getEmployeeList(ListingFilterParameter fp);

    List<EdsEmployee> getLastEmployees();

    List<EdsDepartment> getDepartmentByUser(EdsUser user);

    EdsEmployee getSupervisor(Integer employeeID);

    List<EdsEmployee> getApprovers(EdsEmployee empl, List<String> roleCodes);

    List<EdsEmployee> getUnAssignedEmployees();

    List<EdsEmployee> getEmployeesByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime);

    List<Date> getEmployeesRegisteredByDate(Date startDate, Date endDate);

    List<Date> getEmployeesActivationDates(Date startDate, Date endDate);

    List<EdsEmployee> getCompanyEmployees();

    List<EdsEmployee> getCompanyEmployees(List<Integer> availableItems);

    List<EdsEmployee> getInactiveEmployees();

    List<EdsEmployee> getEmployeesByEmail(String s);

    List<EdsEmployee> getPositionEmployees(EdsPosition position);

    Long getPositionEmployeeCount(Integer id);

    List<EdsEmployee> getPositionEmployees(Integer positionId);

    List getEmployeesForDailyTimeSheetData(EdsUser employee, Date startDate, Date endDate);

    List<EdsEmployee> getEmployees(Integer timeSlotId);

    List<EdsEmployee> getTeamEmployees(Integer departmentId);

    List<EdsEmployee> getActiveTeamEmployees(Integer departmentId);

    List<EdsEmployee> getTeamEmployees(Integer departmentId, EdsUser user);

    Map<Integer, SelectItem> getTeamEmployeesForOrgChart(Integer departmentId, boolean showExternalEmployee, Integer typeExternalId);

    Map<Integer, Integer> getTeamEmployeesCount();

    List<EdsEmployee> getEmployeesForPayroll();

    List<EdsEmployee> getCompanyEmployees(Integer companyid, Integer start, Integer limit);

    void deletePositionEmployee(EdsPosition position);

    List<EdsEmployee> getTeamLocationEmployees(EdsDepartment department, EdsLocation location);

    EdsEmployee getEmployeeByProfileID(Integer profileID);

    Integer getEmployeeByEmail(String email);

    EdsTimeSlot getEmployeeTimeSlot(Integer employeeID);

    List<EdsEmployee> getEmployeesForAccounting(ListingFilterParameter filterParametrs, String[] roles);

    int getEmpoyeeCountByTimeSlot(EdsTimeSlot ts);

    void onRolesChanged(EdsEmployee employee, ArrayList<Integer> removedRoleIDs, ArrayList<Integer> addedRoleIDs, ArrayList<Integer> intersect);

    List getResourceUtilReport(ListingFilterParameter fp);

    String getRolePermission();

    String getRolePermissionForPoject();

    List getOtherDetailsResourceUtilReport(ListingFilterParameter fp);

    List<Integer> getEmployeeIds(ListingFilterParameter fp);

    List<Integer> getEmployeeIDs(ListingFilterParameter fp);

    List<Integer> getProjectIds(ListingFilterParameter fp);

    ArrayList<Integer> getTimesheetRequiredEmployeesID();

    void updateRequired(String employeeIDs);

    void updateAllRequired();

    List<Object[]> getTimesheetBaseData(Integer employeeID, Date startDate, Date endDate);

    EdsEmployee getEmployeeByFirstNameViaLastName(String firstNameViaLastName);

    Integer getEmployeePayslipCount(Integer employeeId);

    List<EmployeePayslipItem> getEmployeePayslips(ListingFilterParameter listingFilterParameter);

    List<Object[]> getTeamEmployees();

    EdsEmployee getEmployeeByDriverNumber(Long driverNumber);

    List<SelectItem> getEmployeeListForPayroll(ListingFilterParameter filterParameter);

    Map<String, EdsEmployee> getEmployeeMapByCode();

    Map<Integer, UserBankAccountData> getEmployeeBankAccountMap();

    HashMap<Integer, HashMap<String, String>> getEmployeesPayrollSettingsMap();

    List<SelectItem> getDriverListAsSelectItems(ListingFilterParameter filterParameter);

    EdsEmployee getEmployeeByNumber(String employeeNumber);

    EdsEmployee getEmployeeByInn(String employeeNumber);

    List<EdsUser> getEmployeesForReminder(String columnCode, Integer companyId, String reminderTimeAction, String endReminderTimeAction);

    SelectItem getEmployeesById(Integer objectID);

    SelectItem getEmployeesWithTeamLeader(Integer objectID);

    List<EdsOnboardingStep> getOnboardingstepForReminder(String columnCode, Integer companyId, String formatDate, String endformatDate);

    ArrayList<Integer> getEmployeePosition(Integer positionID);

    List<EdsEmployee> getEmployeeByPayrollBatch(Integer payrollBatchID);

    List<Integer> getEmployeeIDsByIDs(String IDs);

    List<Integer> getEmployeeIDsWithLimit(Integer startat, Integer limit);

    List<Integer> getCompanyDeletedEmployeeListForSolr(SolrReindexRpc solrReindex);

    List<EdsEmployee> getEmployeeItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    Boolean isIntegerEmployeeCodeEnabled();

    ArrayList<Integer> getLocationEmployees(Integer locationId);

    ArrayList<EdsEmployee> getEmployeesByLocation(Integer locationId);

    List<Object[]> getEmployeesWithDepartment();

    List<Object[]> getEmployeesWithDepartmentByDepartmentAndLocation(EdsDepartment department, EdsLocation location);

    EdsEmployee getEmployeeByContact(EdsCrmContact contact);

    ArrayList<Integer> getSupervisorIDsByEmployee(Integer employeeID);

    List<EdsEmployee> getEmployeesBySupervisorId(Integer supervisorId);

    void updateEmployeesPosition(ArrayList<Integer> oldEmployeeIDs, Integer positionID);

    void setProbationDaystoAllEmployees(Double probationDays);

    List<Integer> getByImportFileID(Integer entityID);

    boolean userIsAssignToTask(Integer objectID, Integer taskID);

    Long[] getEmployeesGenderRatio();

    List<Integer> getChildEmployees(Integer supervisorId);

    List<Integer> getDepartmentAndChildDepartmentEmployeeIds(Integer leadID);

    List<EdsEmployee> getChildEmployeesObject(Integer supervisorId);

    EdsEmployee getEmployeeByPlacementIds(Integer id);

    void updateEmployeeLocation(Set<Integer> employeesId, Integer locationId);

    void updateEmployeeLocationByTeam(Set<Integer> employeesId, EdsLocation location);

    EdsEmployee getEmployeeByUniqueId(String approverUniqueId);

    Long getEmployeesCountByPosition(EdsPosition position);

    ArrayList<SelectItem> getAvailablePositionsForOrgChart(Integer departmentId);

    List<EdsEmployee> getEmployeesForForteensSalary(LocalDate date);

    List<EdsEmployee> getEmployeesByDepartment(Integer departmentId,boolean onlyActiveEmployee);

    List<EdsEmployee> getEmployeesByFilter(ListingFilterParameter fp);

    Integer getEmployeesCountByFilter(ListingFilterParameter filterParameter);

    EdsEmployee getEmployeeByCode(String employeeCode);

    EdsEmployee getEmployeeByPassportNumber(String passportNumber);

    EdsEmployee getEmployeeByFirstAndLastName(String firstName, String lastName);

    List<EdsEmployee> getEmployeesForVerification(String text);

    List<EdsEmployee> getEmployeesByData(ProfileItem profileItem);
}