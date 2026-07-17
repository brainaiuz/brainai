package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.BillOfMaterialItem;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardIssues;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardTasks;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Anvarbek Date: 07.01.2008 Time: 15:12:18 To
 * change this template use File | Settings | File Templates.
 */

public interface ProjectServiceAsync {

    void getClients(AsyncCallback<SelectItem[]> async);

    void getClient(Integer clientID, AsyncCallback<SelectItem> async);

    void getProjectStatuses(AsyncCallback<SelectItem[]> async);

    void viewProject(Integer objectID, AsyncCallback<ProjectViewItem> async);

    void getTeams(AsyncCallback<LinkedList<WfmTreeItem>> async);

    void getInvoiceList(Integer projectId, ListingFilterParameter fp, AsyncCallback<ListResult<ProjectInvoice>> invoiceList);

    void getProjectForEdit(Integer projectId, Date date, Integer clientId, AsyncCallback<EditProject> async);

    void searchClientsByProjectId(Integer projectId, String searchKey, AsyncCallback<SelectItem[]> async);

    void updateProject(EditProject project, AsyncCallback<Void> async);

    void getProjectAttachments(Integer projectID, AsyncCallback<FileResource[]> callback);

    void deleteAttachment(Integer attachmentId, AsyncCallback<Void> async);

    void getProjectEmployees(Integer projectId, AsyncCallback<ProjectMember[]> async);

    void getProjectEmployees(Integer companyID, Integer projectID, AsyncCallback<ProjectMember[]> callback);

    void getCompanyEmployees(AsyncCallback<ProjectMember[]> async);

    void getProjectLabourCostsItems(Integer projectId, AsyncCallback<ProjectLabourCosts[]> async);

    void getProjectCostItems(Integer projectID, AsyncCallback<ProjectViewItem> async);

    void getProjectLabourCostsSubItems(Integer workStreamId, AsyncCallback<ProjectLabourCosts[]> async);

    void saveProjectWageRates(ProjectMember[] members, AsyncCallback<Void> async);

    void saveCloneProject(CloneProjectItem item, AsyncCallback<Integer> async);

    void saveProject(ProjectSingleItem item, AsyncCallback<Integer> async);

    void saveProject(ProjectSingleItem item, boolean fromCsvImport, AsyncCallback<Integer> async);

    void deleteProject(Integer projectId, AsyncCallback<Void> async);

    void getProjectEmployeesForView(Integer projectID, AsyncCallback<KpiTreeInfo[]> async);

    void getProjectEmployeesHistory(Integer projectID, AsyncCallback<KpiTreeInfo[]> async);

    void getDefaultProjectID(AsyncCallback<Integer> async);

    void getProjectResourceLoad(ListingFilterParameter fp, int period, AsyncCallback<DashboardTasks[]> async);

    void getProjectDashboardIssues(ListingFilterParameter fp, int period, AsyncCallback<DashboardIssues[]> async);

    void getProjectNotes(Integer projectID, Integer limit, AsyncCallback<HistoryListItem[]> async);

    void getProjectNotes(Integer projectID, Integer limit, boolean withAllTaskNotes, AsyncCallback<HistoryListItem[]> async);

    void indexProjectTasks(Integer projectID, AsyncCallback<Void> async);

    void saveProjectNoteComments(NewsComment data, AsyncCallback<NewsComment> callback);

    void deleteProjectNoteComment(Integer commentId, AsyncCallback<Void> async);

    void getProjectNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void getProjectsList(Integer viewAs, AsyncCallback<SelectItem[]> async);

    Request getPurchaseOrderList(Integer projectId, ListingFilterParameter fp, AsyncCallback<ListResult<ProjectInvoice>> invoiceList);

    Request getPurchaseInvoiceList(Integer projectId, ListingFilterParameter fp, AsyncCallback<ListResult<ProjectInvoice>> invoiceList);

    Request getExpenseReportList(Integer projectId, ListingFilterParameter fp, AsyncCallback<ListResult<ProjectExpenseReportsListItem>> async);

    void generateProjectNumber(Date date, Integer clientId, Integer projectId, AsyncCallback<NumberData> callback);

    void isDateExistsInNumbering(AsyncCallback<Boolean> callback);

    void isClientExistsInNumbering(AsyncCallback<Boolean> callback);

    void getProjectList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ProjectListItem>> callback);

    void saveProjectEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus, AsyncCallback<Void> callback);

    void getProjectEmployeeWageClientHistory(Integer projectEmployeeId, AsyncCallback<ProjectEmployeeWageClientHistoryItem[]> callback);

    void updateProjectEmployeeWageClientHistory(ProjectEmployeeWageClientHistoryItem[] hist, Integer projectEmployeeId, Integer projectId, AsyncCallback<Void> async);

    void calculateProjectBudgets(Integer objectID, Boolean isClearAndReCalculate, AsyncCallback<Void> async);

    void deleteProjectEmployeeWageClientRateHistory(Integer historyId, AsyncCallback<Void> async);

    void getProjectDetailsFrom(String projectFrom, Integer projectFromID, AsyncCallback<EditProject> asyncCallback);

    void getParentIsNullProjects(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getProjectBudgetItems(Integer projectID, boolean withTax, AsyncCallback<ProjectBudget> async);

    void getEmployeeCostClientCharge(Integer projectID, AsyncCallback<ProjectBudget> async);

    void getTimeSheetClients(AsyncCallback<SelectItem[]> async);

    void getBookingItems(ListingFilterParameter filterParameter, AsyncCallback<ListResult<BookingItemsItem>> callback);

    void getBookingItemsData(Integer bookingItemsId, AsyncCallback<BookingItemsItem> async);

    void getBookingItemsReservationData(Integer reservationItem, AsyncCallback<BookingReservationItem> async);

    void getBookingItemsReservationHistory(Integer bookingItemId, AsyncCallback<ListResult<BookingReservationItem>> async);

    void deleteBookingItem(Integer int_objectID, AsyncCallback<Void> callback);

    void getBookingItemsReservationHistoryList(Integer bookingItemId, AsyncCallback<ArrayList<BookingReservationItem>> async);

    void saveBookingItem(BookingItemsItem item, AsyncCallback<Integer> async);

    void saveBookingItemReservation(BookingReservationItem item, AsyncCallback<Integer> async);

    void validateBookingItemReservation(BookingReservationItem item, AsyncCallback<Integer> async);

    void generateBookingItemNumber(AsyncCallback<NumberData> callback);

    void getProjectPeriod(Integer projectID, AsyncCallback<Date[]> callback);

    void getNewProjectBudgetData(Integer projectID, DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<NewProjectBudgetData> callback);

    void getProjectBudgetRowDataByAccount(Integer projectID, Integer accountID, ArrayList<DateNonConvertable[]> monthIntervalsList, String type, AsyncCallback<NewProjectBudgetRowItem> callback);

    void saveProjectBudgetData(NewProjectBudgetData budgetData, AsyncCallback<Void> callback);

    void getAccountsForProjectBudget(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> callback);

    void getSaleQuoteList(Integer projectId, ListingFilterParameter fp, AsyncCallback<ListResult<ProjectInvoice>> async);

    void getProjectSpecificPermissions(Integer projectID, AsyncCallback<HashSet<String>> async);

    void getCalendarEventById(Integer clientID, AsyncCallback<SelectItem> async);

    void getBookingItemsByCategoryId(Integer viewAs, AsyncCallback<SelectItem[]> async);

    void getBookingItemReservation(Integer reservationID, AsyncCallback<BookingReservationItem> callback);

    void isProjectNumberExists(String numberString, Integer projectID, AsyncCallback<Boolean> callback);

    void deleteReservation(Integer reservationID, AsyncCallback<Void> async);

    void updateProjectStatus(HashSet<ProjectListItem> projectListItemSet, SelectItem status, boolean updateProjectStatus, AsyncCallback<Void> abstractAsyncCallback);

    void getProjectPositions(Integer projectID, AsyncCallback<ProjectPosition[]> async);

    void getContractList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ContractListItem>> callback);

    void deleteContract(Integer objectId, AsyncCallback<Void> callback);

    void saveContract(ContractSingleItem newContract, AsyncCallback<Integer> callback);

    void updateContract(EditContract contract, AsyncCallback<Void> asyncCallback);

    void getContractForEdit(Integer contractID, AsyncCallback<EditContract> callback);

    void viewContract(Integer contractID, AsyncCallback<ContractViewItem> callback);

    void getProjectTimesheets(Integer projectID, AsyncCallback<TaskTimeEntriesItem[]> callback);

    void getProjectClients(Integer projectID, AsyncCallback<SelectItem[]> callback);

    void getProjectAsLookupItem(Integer id, AsyncCallback<SelectItem> async);

    void getManagers(AsyncCallback<HashSet<SelectItem>> abstractAsyncCallback);

    void fillBillOfMaterialItemsWithInventory(Integer projectID, AsyncCallback<BillOfMaterialItem[]> async);

    void saveBillOfMaterialItems(Integer projectID, String status, BillOfMaterialItem[] item, String message, AsyncCallback<Integer> async);

    void getBillOfItemsStatus(Integer projectID, AsyncCallback<String[]> callback);

    void unfreezeBOM(Integer projectID, AsyncCallback<String> callback);

    void saveRequestedBillOfMaterial(Integer projectID, ArrayList<BillOfMaterialItem> items, AsyncCallback<Integer> async);

    void getReferenceByCode(String referenceCode, AsyncCallback<ArrayList<SelectItem>> async);

    void getNearbyProjects(Double latitude, Double longitude, Integer radius, List<Integer> assigneeIds, List<String> statusCodes, List<String> excludeStatusCodes, AsyncCallback<List<NearbyProjectDto>> callback);
}
