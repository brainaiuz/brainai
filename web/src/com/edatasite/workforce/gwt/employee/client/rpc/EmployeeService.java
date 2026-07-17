package com.edatasite.workforce.gwt.employee.client.rpc;

import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User:  Date: 07.01.2008 Time: 16:23:15
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeeService extends RemoteService {

    EmployeeViewItem getEmployee(Integer objectID);

    //Integer getEmployeeIdByQBId(String quickbookId);

    SelectItem[] getRoles();

    Integer createEmployee(NewEmployee employee, boolean fromSignUp);

    Integer[] createEmployees(NewEmployee[] employees);

    void updateEmployeePaySettings(Integer employeeID, KeyValueStruct[] payrollSettings);

    ListResult<EmployeeListItem> getEmployees(ListingFilterParameter filterParametrs);

    Integer saveEmployeeEditCellValue(EmployeeListItem rowValue, String columnCodeName);

    ArrayList<DeparmentEmployees> getEmployeesWithTeams();

    SelectItem[] getCompanyEmployeesAsSelectItems();

    SelectItem[] getEmployeesAaSelectItemsByDepartmentId(Integer departmentID);

    SelectItem[] getCompanyEmployeesForPayroll();

    Boolean changeToESSEmployee(Integer employeeID);

    void activateOrDisactivateEmployee(Integer employeeID, Boolean activate);

    Boolean grantAccessToEmployee(Integer employeeID, Boolean grantAccess);

    Boolean grantAccessToEmployeeWithEss(Integer employeeID, Boolean grantAccess, boolean isEss);

    void resendActivationLink(Integer employeeID);

    ProjectMember[] getProjectEmployeesWithTeams(Integer projectID);

    ProjectMember[] getProjectMembers(Integer projectId, Integer employeeId);

    ProjectMember[] getProjectMembersAll(Integer projectId);

    ProjectMember[] getProjectEmployees(Integer projectID);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getProjectEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getBrigadaEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole);


    ArrayList<KpiTreeInfo> getPositionEmployees(ListingFilterParameter fp, Map<Integer, HashMap<String, KpiTreeInfo>> map);

    Integer getProjectEmployeeEditablePermmission(Integer projectId);

    SelectItem[] getRegions(Integer countryId);

    DepartmentItem[] getDepartmentsSelectItem();

    SelectItem[] getCountries();

    String getEmployeeName(Integer employeeId);

    TeamEmployee getTeamByEmployeeId(Integer employeeId);

    TeamEmployee getManagerByEmployeeId(Integer employeeId);

    void deleteEmployee(Integer employeeId, boolean removeContact, boolean isRemove, DateNonConvertable resignationDate, ReferenceItem rejectionReason);

    void deleteEmployees(ArrayList<Integer> employeeId, boolean removeContact);

    void removeDepartmentLeader(Integer[] teamsId, Integer employeeId, Integer moveEmployeeId);

    void removeProjectManagers(Integer[] projectId, Integer employeeId, Integer moveEmployeeId);

    int getEmployeesMaxCount();

    Integer[] getUserLimit();

    Integer[] getAllEmployeesMaxCount(Integer companyID, Integer exceptEmployee);

    HistoryListItem[] getEmployeeNotes(Integer employeeID);

    NewsComment saveEmployeeNoteComments(NewsComment data);

    NewsComment[] getEmployeeNoteComments(Integer noteID);

    String getOrgChart();

    SelectItem[] getCompamyLocaleList();

    SelectItem[] getProject(ListingFilterParameter filterParametrs);

    SelectItem[] getProjectsAsSelectItem(ListingFilterParameter filterParametrs);

    ProjectMember getProjectMemberByEmployee(Integer employeeID);

    HashSet<String> getEmployeeSpecificPermission(Integer departmentID, String sectionContext);

    String getEmployeeRoles(Integer employeeID);

    Integer[] createEmployeesForQB(NewEmployee[] employees);

    ListResult<EmployeePayslipItem> getEmployeePayslips(ListingFilterParameter listingFilterParameter);

    Integer getEmployeesMaxCountByCompanyId(Integer companyId);

    SelectItem[] getProjectEmployeesAsSelectItem(ListingFilterParameter fp);

    ListResult<EmployeeListItem> getEmployeeList(ListingFilterParameter filterParametrs);

    LinkedHashMap<String, Integer> getHeadcountChartData(ListingFilterParameter fp);

    LinkedHashMap<String, Double> getEmployeeByStatusChartData(ListingFilterParameter fp);

    LinkedHashMap<Double[], List<SelectItem>> getNewEmployeeJoiningRatio(ListingFilterParameter fp);

    void convertEmployeeToCandidate(HashSet<EmployeeListItem> selectedItems);

    String getEmployeeGraphChart(boolean isShowView, Integer levelOptionList, boolean levelActive);

    Integer getLevelOfEmployees();

    int orgChartViewSize();

    int checkEmployeeForApprovers(Integer employeeID);

    SelectItem[] getEmployeesForShiftAsSelectItem(ListingFilterParameter filterParameter);

    List<EmployeeListItem> checkMultipleEmployeesForApprovers(List<EmployeeListItem> employees);

    Boolean checkPositionAvailability(Integer positionId);

    Boolean checkPositionAvailability(ArrayList<Integer> positionId);

    List<ProfileItem> getEmployeeForVerification(String text);

    List<ProfileItem> getEmployeesByData(ProfileItem profileItem);

    class App {
        public static EmployeeServiceAsync get() {
            ServiceDefTarget target = GWT.create(EmployeeService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/employee");
            return (EmployeeServiceAsync) target;
        }
    }

}
