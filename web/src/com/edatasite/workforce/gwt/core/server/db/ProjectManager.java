package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBillOfMaterial;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ExportToExcelItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;
import com.edatasite.workforce.gwt.project.client.rpc.NearbyProjectDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User:
 * Date: 07.01.2008
 * Time: 15:25:20
 */
public interface ProjectManager extends Manager<EdsProject> {

    List<EdsProject> list() throws NullPointerException;

    List<Object[]> getProjectEmployees(Integer projectId);

    List<EdsEmployee> getProjectUnavailableEmployees(EdsProject project, Date startDate, Date endDate);

    List<Object[]> getProjectEmployees(Integer projectId, Integer userMaxRole);

    List<Object[]> getProjectEmployees(EdsProject projectFilter, Integer viewAsFilter);

    List<Object[]> getProjectEmployees(EdsProject projectFilter, Integer viewAsFilter, EdsUser user);

    SelectItem[] getProjectEmployeeList(String projectIds, Integer maxRole, EdsUser user);

    List<EdsProject> getEmployeeManagedProjects(EdsEmployee employee);

    SelectItem[] getTasksCountByProject(Integer projectID);

    List<EdsProjectEmployee> getProjectInvolvedEmployees(EdsProject project);

    List<EdsProject> getLastProjects();

    List<Integer> getCompaniesByProjectRegDate(Date sTime, Date eTime);

    List<EdsProject> getProjectsByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime);

    List<Object[]> getResult(EdsCrmAccount client, EdsProject project,
                             EdsDepartment department, EdsEmployee employee, Integer viewAsId,
                             String groupByName, Date from, Date to, boolean showClient,
                             boolean showProject, boolean showDepartment, boolean showEmployee,
                             boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                             boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus,
                             boolean showEstimatedTime, boolean showManagerComment);

    List<Object[]> getResult(EdsCrmAccount client, EdsProject project,
                             EdsDepartment department, EdsEmployee employee, Integer viewAsId,
                             String groupByName, Date from, Date to, boolean showClient,
                             boolean showProject, boolean showDepartment, boolean showEmployee,
                             boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                             boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus,
                             boolean showEstimatedTime, boolean showManagerComment, boolean skipRoleChecking);


    List<Object[]> getResults(List<Integer> clientFilter, List<Integer> project,
                              EdsDepartment department, List<Integer> employeeIDs, Integer viewAsId,
                              String groupByName, Date from, Date to, boolean showClient,
                              boolean showProject, boolean showDepartment, boolean showEmployee,
                              boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                              boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus, Integer formType, boolean showEstimatedTime,
                              boolean showManagerComment, boolean skipRoleChecking);

    List<EdsProject> list(ListingFilterParameter fp);

    List<EdsProject> list(ListingFilterParameter fp, EdsUser user);

    List<EdsProject> projectsList(ListingFilterParameter fp, EdsUser user);

    List<EdsProject> projectsList(ListingFilterParameter fp);

    List<EdsProjectEmployee> getEmployeesByProject(Integer projectId);

    ArrayList<EdsEmployee> getEmployeesObjectByProject(Integer projectId);

    List<EdsProjectEmployee> getEmployeesByProject(Integer projectId, Integer employeeId);

    ArrayList<EdsProjectEmployee> getEmployeesByProjectAndEmployee(Integer projectId, String employeeName);

    List<EdsProjectEmployee> getProjectEmployeesHistoryByProject(Integer projectID);

    List<EdsProjectEmployee> getEmployeesByProjectAll(Integer projectId);

    List<EdsProject> getUserProjects(EdsUser user);

    List<EdsProject> getCompanyProjects();

    List<Integer> getCompanyDeleteProjectsForSolr(SolrReindexRpc solrReindex);

    List<EdsProject> getCompanyProjectsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsProjectEmployee> getEmployeeNotStartedOnGoingProjects(EdsEmployee employee);

    ArrayList<SelectItem> getEmployeeNotStartedOnGoingProjectsAsSelectItem(EdsEmployee employee);

    void deleteProject(EdsProject project);

    List<EdsProject> getProjectsByClientID(Integer clientID);

    EdsProjectEmployee getProjectEmployee(Integer projectEmployeeId);

    List<EdsProject> getChoosesProjectDashboard(Integer proejctId);

    List getProjectsForDashboard(String projectIds);

    List<EdsCrmAccount> getPMClients();

    List<EdsProject> getProjectClients(Integer clientId);

    List getDepartmentInvolvedProjectCounted(Integer departmentId, Integer employeeId, boolean status);

    List<EdsProject> getProjects(String projectIds);

    List<EdsProject> getCalendarProjects(List<Integer> employeeIDs, Date start, Date end);

    List<EdsProject> getProjectManagersByEmployeeId(Integer employeeId, boolean activeOnly);

    ListingObjectItem getInvoiceList(Integer projectId, ListingFilterParameter fp);

    ListingObjectItem getpurchaseOrderList(Integer projectId, ListingFilterParameter fp);

    ListingObjectItem getPurchaseInvoiceList(Integer projectId, ListingFilterParameter fp);

    ListingObjectItem getExpenseReportList(Integer projectId, ListingFilterParameter fp);

    Integer getProjectLastIntNumber();

    boolean isProjectNumberExists(String number, Integer projectID);

    EdsSaleInvoice getProjectLastInvoice(Integer projectId);

    String getProjectLastInvoiceNumber(Integer projectId);

    List<Integer> getProjectIdList(String ids);

    List<Integer> getCompanyProjectIdList(Integer companyID, Integer start, Integer limit);

    List<EdsProjectEmployeeWageClientRateHistory> getEmployeeWageClientRateHistory(Integer employeeId, Integer projectId);

    void deleteEmployeeWageClientRateHistory(Integer historyId);

    void updateEmployeeWageClientRateHistory(EdsProjectEmployeeWageClientRateHistory hist);

    void updateEmployeeWageClientRateHistorybyDate(EdsProjectEmployeeWageClientRateHistory hist);

    void updateProjectEmployeeOb(Double WR, Double CChr, Integer employeeId, Integer projectId);

    void clearProjectBudgetCalculatedItems(Integer projectID);

    List<EdsProject> getProjectsParentIsNull(Integer projectId);

    List<EdsProject> getSubProjects(EdsProject parent);

    List<Integer> getSubProjectIDs(Integer parentProjectID);

    List<Integer> getPMManagedProjectsEmployeeIDs(Integer employeeId);

    List<EdsExpenseReport> getAllExpenseReports(ListingFilterParameter filterParametrs);

    EdsProject getCrmProject();

    List<Integer> getProjectIDsByProjectIDs(List<Integer> arrayList);

    Map<Integer, Long> getProjectTaskCounts(List<Integer> projectIds);

    List<Integer> getCustomerIDsByProjectManager(EdsUser edsUser);

    ListingObjectItem getSaleQuoteList(Integer projectId, ListingFilterParameter fp);

    boolean isDepartmentLeaderOfProject(Integer projectID, Integer userID);

    List getResourceUtilProjectReport(Date startDate, Date endDate, Integer start, ListingFilterParameter filterParameter);

    List getResourceUtilTaskReport(Date startDate, Date endDate, Integer start, ListingFilterParameter filterParameter);

    List<Object[]> getProjectNumberById(Integer projectID);

    String getSavedNumberformat(Integer objectID);

    List<EdsPickList> getProjectRelatedPickLists(Integer projectID);

    List<EdsSaleQuote> getProjectRelatedSalesQuotes(Integer projectID);

    List<EdsPurchaseOrder> getProjectRelatedPurchaseOrders(Integer projectID);

    List<EdsSaleInvoice> getProjectRelatedSalesInvoices(Integer projectID);

    List<EdsPurchaseInvoice> getProjectRelatedPurchaseInvoices(Integer projectID);

    List<EdsExpenseReport> getProjectRelatedExpenseReports(Integer projectID);

    EdsProjectEmployeeWageClientRateHistory getProjectEmployeeWageClientRateHistory(Integer wageRateHistoryID);

    EdsProjectEmployeeWageClientRateHistory getNextProjectEmployeeWageClientRateHistory(Integer wageRateHistoryID);

    void updateTimesheetWageRates(EdsProjectEmployeeWageClientRateHistory nextWageRate, EdsProjectEmployeeWageClientRateHistory deletedWageRate);

    void updateTimesheetWageRates(Double wageRate, Double clientChargeRate, Integer employeeID, Integer projectID, Date from, Date to);

    void updateTimesheetWageRates(Integer employeeID, Integer projectID);

    Date getProjectActualStartDate(Integer projectID);

    Date getProjectActualEndDate(Integer projectID);

    ExportToExcelItem getResourceUtilEmployeeExcel(ListingFilterParameter filterParameter, Boolean projectSelected);

    List<EdsProject> getProjectByName(String name);

    Float getProjectActualPercentCompleted(Integer projectID);

    List<EdsProject> getProjectByCompanyID(Integer companyId);

    List<EdsProjectPosition> getProjectPositions(Integer projectID);

    EdsProject getProjectByNumber(String parentNumber);

    Map<String, Integer> getProjectAsMapByNumber();

    Map<String, Integer> getProjectAsMap();

    ArrayList<Integer> getProjectClientsByID(Integer projectID);

    List getEmployeeSalaryReport(Integer employeeID, String period, Integer payslipItemId);

    void deleteProjectBillOfItems(Integer projectID);

    List<Object[]> getCountByStatus();

    Long getTotalTimeSpent();

    Long getTotalEstimated();

    Long getTotalCount();

    List<EdsProject> getProjectsByDate(ListingFilterParameter fp);

    Map<Integer, EdsBillOfMaterial> getBomAsMap(Integer projectID);

    void deleteBillOfItems(List<Integer> ids);

    EdsCrmContact getRelationContact(String projectNumber);

    List<NearbyProjectDto> getNearbyProjects(Double latitude, Double longitude, Integer radius, List<Integer> assigneeIds, List<String> statusCodes, List<String> excludeStatusCodes);
}
