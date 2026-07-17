package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public interface ReportService extends RemoteService {
    SelectItem[] getPriorities();

    SelectItem[] getRoleList();

    Integer getProjectStatusIDAll();

    SelectItem[] getUserMaxRolesWithMEM();

    SelectItem[] getProjectListForReport(Integer clientId, Integer departmentId, Integer employeeId, Integer viewAsId, Integer statusId);

    SelectItem[] getProjectListForReport(Integer projectId, String startDate, String endDate, Integer clientId, Integer departmentId, Integer employeeId, Integer statusId);

    SelectItem[] getDepartmentList(Integer clientId, Integer projectId, Integer employeeId, Integer viewAsId);

    SelectItem[] getEmployeesList(Integer clientId, Integer projectId, Integer departmentId, Integer viewAsId);

    SelectItem[] getTaskEmployeesList(Integer taskId);

    SelectItem[] getEmployeesListForRU(Integer clientId, Integer projectId, Integer departmentId);

    SelectItem[] getEmployeesList(Integer companyId);

    SelectItem[] getClientsList(Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId);

    SelectItem[] getStatuses(Integer projectId);

    SelectItem[] getReccuringInvoiceStatuses();

    SelectItem[] getContractList();

    SelectItem[] getEmployeeStatusList();

    SelectItem[] getAttendanceTerminalSelectItems();

    SelectItem[] getStatusesForFilterDrop();

    SelectItem[] getTimesheetFilterStatusesDrop();

    SelectItem[] getInvoiceQuoteStatuses(String type);

    SelectItem[] getClientList();

    SelectItem[] getTimesheetApprovalStatusList();

    SelectItem[] getTimesheetApprovers();

    void deleteAttachment(Integer attachmentId);

    void deleteAttachment(Integer attachmentId, Integer companyId);

    FileItem[] getExpenseAttachments(Integer expenseId);

    SelectItem[] getSupplier();

    Integer[] getEmployeesMaxCount(Integer excepEmployee);

    Integer saveLocation(CompLocationRpc compLocationRpc);

    CompLocationRpc getLocation(Integer locationId);

    Integer updateLocation(CompLocationRpc location);

    SelectItem[] getCityOrDistrictByRegionId(Integer regionId);

    SelectItem[] getCountryList();

    SelectItem[] getLocationList();

    SelectItem[] getProjectList();

    SelectItem[] getLeadStatuses();

    SelectItem[] getOpportunityStages();

    SelectItem[] getCampaignsNameList();

    SelectItem[] getActivityStatuses();

    SelectItem[] getCampaignStatus();

    SelectItem[] getIssuePriorities();

    SelectItem[] getIssueStatuses();

    SelectItem[] getSalaryGradeListItems();

    FileResource[] getRelatedFiles(Integer employeeId);

    SelectItem[] getPositionsList(ListingFilterParameter fp);

    Integer createPosition(NewPosition position);

    SelectItem[] getCompanyTimeSlots();

    Boolean isFeatureShown(String message_code);

    Boolean isEmployeeNumberExists(String empCode, Integer objectID, String from);

    SelectItem[] getEmailTemplateCategoriesByList(ListingFilterParameter fp);

    SelectItem[] getUsersByNews();

    SelectItem[] getNewsCategories();

    SelectItem[] getEmplyeePositionList();

    SelectItem[] getCertificateTypes();

    HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter fp, String formType);

    HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getTeamsMap(ListingFilterParameter fp, String formType);

    void saveEmployeeLocation(HashSet<Integer> locationMembers, Integer objectID, boolean isChecked);

    EmployeeListItem[] getEmployeesForGrid(ListingFilterParameter fp);

    SelectItem[] getAssessmentTypeList();

    SelectItem[] getAssessmentStatusList();

    SelectItem[] getAgentIDs();

    SelectItem[] getValidityPeriodList();

    SelectItem[] getBugStatusList();


    class App {
        public static ReportServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/report");
            return (ReportServiceAsync) target;
        }
    }

}
