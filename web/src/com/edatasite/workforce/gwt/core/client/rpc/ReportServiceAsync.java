package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public interface ReportServiceAsync {

    void getRoleList(AsyncCallback<SelectItem[]> callback);

    void getUserMaxRolesWithMEM(AsyncCallback<SelectItem[]> callback);

    void getInvoiceQuoteStatuses(String type, AsyncCallback<SelectItem[]> callback);

    Request getPriorities(AsyncCallback<SelectItem[]> callback);

    void getClientsList(Integer projectId, Integer departmentId,
                        Integer employeeId, Integer viewAsId, AsyncCallback<SelectItem[]> callback);

    void getContractList(AsyncCallback<SelectItem[]> callback);

    void getProjectStatusIDAll(AsyncCallback<Integer> callback);

    void getProjectListForReport(Integer clientId, Integer departmentId,
                                 Integer employeeId, Integer viewAsId, Integer statusId,
                                 AsyncCallback<SelectItem[]> callback);

    void getProjectListForReport(Integer projectId, String startDate, String endDate, Integer clientId, Integer departmentId,
                                 Integer employeeId, Integer statusId,
                                 AsyncCallback<SelectItem[]> callback);

    void getDepartmentList(Integer clientId, Integer projectId,
                           Integer employeeId, Integer viewAsId, AsyncCallback<SelectItem[]> callback);

    void getEmployeesList(Integer clientId, Integer projectId,
                          Integer departmentId, Integer viewAsId, AsyncCallback<SelectItem[]> callback);

    void getTaskEmployeesList(Integer taskId, AsyncCallback<SelectItem[]> callback);

    void getEmployeesListForRU(Integer clientId, Integer projectId,
                               Integer departmentId, AsyncCallback<SelectItem[]> callback);

    void getEmployeesList(Integer companyId, AsyncCallback<SelectItem[]> callback);

    void getStatusesForFilterDrop(AsyncCallback<SelectItem[]> callback);

    void getStatuses(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getReccuringInvoiceStatuses(AsyncCallback<SelectItem[]> callback);

    void getEmployeeStatusList(AsyncCallback<SelectItem[]> callback);

    void getAttendanceTerminalSelectItems(AsyncCallback<SelectItem[]> callback);

    void getTimesheetFilterStatusesDrop(AsyncCallback<SelectItem[]> callback);

    void getClientList(AsyncCallback<SelectItem[]> callback);

    void deleteAttachment(Integer attachmentId, AsyncCallback callback);

    void deleteAttachment(Integer attachmentId, Integer companyId, AsyncCallback callback);

    void getExpenseAttachments(Integer expenseId, AsyncCallback<FileItem[]> callback);

    void getSupplier(AsyncCallback<SelectItem[]> callback);

    void getEmployeesMaxCount(Integer exceptEmployee, AsyncCallback<Integer[]> async);

    void saveLocation(CompLocationRpc compLocationRpc, AsyncCallback<Integer> async);

    void getLocation(Integer locationId, AsyncCallback<CompLocationRpc> async);

    void updateLocation(CompLocationRpc location, AsyncCallback<Integer> async);

    void getCityOrDistrictByRegionId(Integer regionId, AsyncCallback<SelectItem[]> async);

    void getCountryList(AsyncCallback<SelectItem[]> async);

    void getLocationList(AsyncCallback<SelectItem[]> async);

    void getProjectList(AsyncCallback<SelectItem[]> async);

    void getLeadStatuses(AsyncCallback<SelectItem[]> async);

    void getOpportunityStages(AsyncCallback<SelectItem[]> async);

    void getCampaignsNameList(AsyncCallback<SelectItem[]> async);

    void getActivityStatuses(AsyncCallback<SelectItem[]> async);

    void getCampaignStatus(AsyncCallback<SelectItem[]> async);

    void getIssuePriorities(AsyncCallback<SelectItem[]> callback);

    void getIssueStatuses(AsyncCallback<SelectItem[]> callback);

    void getSalaryGradeListItems(AsyncCallback<SelectItem[]> async);

    void getPositionsList(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void createPosition(NewPosition position, AsyncCallback<Integer> callback);

    void getTimesheetApprovalStatusList(AsyncCallback<SelectItem[]> async);

    void getTimesheetApprovers(AsyncCallback<SelectItem[]> async);

    void getRelatedFiles(Integer employeeId, AsyncCallback<FileResource[]> async);

    void getCompanyTimeSlots(AsyncCallback<SelectItem[]> callback);

    void isFeatureShown(String message_code, AsyncCallback<Boolean> callback);

    void isEmployeeNumberExists(String empCode, Integer objectId, String from, AsyncCallback<Boolean> callback);

    void getEmailTemplateCategoriesByList(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void getUsersByNews(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getNewsCategories(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getEmplyeePositionList(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getCertificateTypes(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getEmployeesMap(ListingFilterParameter fp, String formType, AsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>> async);

    void getTeamsMap(ListingFilterParameter fp, String formType, AsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>> async);

    void saveEmployeeLocation(HashSet<Integer> locationMembers, Integer objectID, boolean isChecked, AsyncCallback<Void> async);

    void getEmployeesForGrid(ListingFilterParameter fp, AsyncCallback<EmployeeListItem[]> abstractAsyncCallback);

    void getAssessmentTypeList(AsyncCallback<SelectItem[]> callback);

    void getAssessmentStatusList(AsyncCallback<SelectItem[]> callback);

    void getAgentIDs(AsyncCallback<SelectItem[]> callback);

    void getValidityPeriodList(AsyncCallback<SelectItem[]> callback);

    Request getBugStatusList(AsyncCallback<SelectItem[]> callback);
}
