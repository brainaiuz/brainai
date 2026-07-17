package com.edatasite.workforce.gwt.employee.client.rpc;

import com.edatasite.workforce.gwt.client.client.rpc.EmployeePayslipItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: 07.01.2008
 * Time: 16:23:46
 * To change this template use File | Settings | File Templates.
 */

public interface EmployeeServiceAsync {

    void getEmployee(Integer objectID, AsyncCallback<EmployeeViewItem> async);

    void getRoles(AsyncCallback<SelectItem[]> async);

    void createEmployee(NewEmployee employee, boolean fromSignUp, AsyncCallback<Integer> async);

    void createEmployees(NewEmployee[] employees, AsyncCallback<Integer[]> async);

    void checkPositionAvailability(Integer positionId, AsyncCallback<Boolean> asyncCallback);

    void checkPositionAvailability(ArrayList<Integer> positionId, AsyncCallback<Boolean> asyncCallback);

    void updateEmployeePaySettings(Integer employeeID, KeyValueStruct[] payrollSettings, AsyncCallback<Void> async);

    Request getEmployees(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<EmployeeListItem>> async);

    void saveEmployeeEditCellValue(EmployeeListItem rowValue, String columnCodeName, AsyncCallback<Integer> callback);

    void getEmployeesWithTeams(AsyncCallback<ArrayList<DeparmentEmployees>> async);

    void getCompanyEmployeesAsSelectItems(AsyncCallback<SelectItem[]> async);

    void getEmployeesAaSelectItemsByDepartmentId(Integer departmentId, AsyncCallback<SelectItem[]> async);

    void getCompanyEmployeesForPayroll(AsyncCallback<SelectItem[]> async);

    void changeToESSEmployee(Integer employeeID, AsyncCallback<Boolean> async);

    void activateOrDisactivateEmployee(Integer employeeID, Boolean activate, AsyncCallback<Void> async);

    void grantAccessToEmployee(Integer employeeID, Boolean grantAccess, AsyncCallback<Boolean> async);

    void resendActivationLink(Integer employeeID, AsyncCallback<Void> async);

    void getProjectEmployeesWithTeams(Integer projectID, AsyncCallback<ProjectMember[]> async);

    void getProjectMembers(Integer projectId, Integer employeeId, AsyncCallback<ProjectMember[]> async);

    void getProjectMembersAll(Integer projectId, AsyncCallback<ProjectMember[]> async);

    void getProjectEmployees(Integer projectID, AsyncCallback<ProjectMember[]> async);

    void getRegions(Integer countryId, AsyncCallback<SelectItem[]> async);

    void getDepartmentsSelectItem(AsyncCallback<DepartmentItem[]> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getEmployeeName(Integer employeeId, AsyncCallback<String> async);

    void getTeamByEmployeeId(Integer employeeId, AsyncCallback<TeamEmployee> async);

    void getManagerByEmployeeId(Integer employeeId, AsyncCallback<TeamEmployee> async);

    void deleteEmployee(Integer employeeId, boolean removeContact, boolean isRemove, DateNonConvertable resignationDate, ReferenceItem rejectionReason, AsyncCallback<Void> async);

    void removeDepartmentLeader(Integer[] teamsId, Integer employeeId, Integer moveEmployeeId, AsyncCallback<Void> async);

    void removeProjectManagers(Integer[] projectId, Integer employeeId, Integer moveEmployeeId, AsyncCallback<Void> async);

    void getEmployeesMaxCount(AsyncCallback<Integer> async);

    void getUserLimit(AsyncCallback<Integer[]> async);

    void getAllEmployeesMaxCount(Integer companyID, Integer exceptEmployee, AsyncCallback<Integer[]> async);

    void getEmployeeNotes(Integer employeeID, AsyncCallback<HistoryListItem[]> async);

    void saveEmployeeNoteComments(NewsComment data, AsyncCallback<NewsComment> callback);

    void getEmployeeNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void getCompamyLocaleList(AsyncCallback<SelectItem[]> callback);

    //void getEmployeeIdByQBId(String quickbookId, AsyncCallback<Integer> async);

    //void updateEmployeeByQB(NewEmployee employee, String externalGUID, Integer synchItemId, AsyncCallback<Void> async);

    void getOrgChart(AsyncCallback<String> callback);

    void getProject(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getProjectsAsSelectItem(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getProjectMemberByEmployee(Integer employeeID, AsyncCallback<ProjectMember> async);

    void getEmployeeSpecificPermission(Integer objectID, String hrmsContext, AsyncCallback<HashSet<String>> callback);

    void getEmployeeRoles(Integer employeeID, AsyncCallback<String> callback);

    void convertEmployeeToCandidate(HashSet<EmployeeListItem> selectedItems, AsyncCallback<Void> async);

    void getProjectEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void getBrigadaEmployeesForAddEdit(Integer projectID, boolean hasEmployeeAssignRole, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);


    void getPositionEmployees(ListingFilterParameter fp, Map<Integer, HashMap<String, KpiTreeInfo>> map, AsyncCallback<ArrayList<KpiTreeInfo>> async);

    void getProjectEmployeeEditablePermmission(Integer projectId, AsyncCallback<Integer> async);

    void createEmployeesForQB(NewEmployee[] employees, AsyncCallback<Integer[]> async);

    void getEmployeePayslips(ListingFilterParameter listingFilterParameter, AsyncCallback<ListResult<EmployeePayslipItem>> async);

    void getEmployeesMaxCountByCompanyId(Integer companyId, AsyncCallback<Integer> callback);

    void getProjectEmployeesAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    Request getEmployeeList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<EmployeeListItem>> async);

    void getHeadcountChartData(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, Integer>> async);

    void getEmployeeByStatusChartData(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, Double>> asyncCallback);

    void getNewEmployeeJoiningRatio(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<Double[], List<SelectItem>>> asyncCallback);

    void getEmployeeGraphChart(boolean isShowView, Integer levelOptionList, boolean levelActive, AsyncCallback<String> callback);

    void orgChartViewSize(AsyncCallback<Integer> async);

    void getLevelOfEmployees(AsyncCallback<Integer> async);

    void checkEmployeeForApprovers(Integer employeeID, AsyncCallback<Integer> callback);

    void checkMultipleEmployeesForApprovers(List<EmployeeListItem> employeeID, AsyncCallback<List<EmployeeListItem>> callback);

    void grantAccessToEmployeeWithEss(Integer employeeID, Boolean grantAccess, boolean isEss, AsyncCallback<Boolean> async);

    void getEmployeesForShiftAsSelectItem(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void deleteEmployees(ArrayList<Integer> employeeId, boolean removeContact, AsyncCallback<Void> async);

    void getEmployeeForVerification(String text, AsyncCallback<List<ProfileItem>> async);

    void getEmployeesByData(ProfileItem profileItem, AsyncCallback<List<ProfileItem>> async);
}
