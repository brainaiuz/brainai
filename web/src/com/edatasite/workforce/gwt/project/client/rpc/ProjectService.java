package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Anvarbek Date: 07.01.2008 Time: 14:28:35 To
 * change this template use File | Settings | File Templates.
 */

public interface ProjectService extends RemoteService {

    SelectItem[] getClients();

    SelectItem[] getProjectStatuses();

    ProjectViewItem viewProject(Integer objectID);

    LinkedList<WfmTreeItem> getTeams();

    ListResult<ProjectInvoice> getInvoiceList(Integer projectId, ListingFilterParameter fp);

    EditProject getProjectForEdit(Integer projectId, Date date, Integer clientID);

    SelectItem[] searchClientsByProjectId(Integer projectId, String searchKey);

    void updateProject(EditProject project) throws NumberExistingException;

    FileResource[] getProjectAttachments(Integer projectID);

    void deleteAttachment(Integer attachmentId);

    SelectItem[] getProjectsList(Integer viewAs);

    ProjectMember[] getProjectEmployees(Integer companyID, Integer projectID);

    ProjectMember[] getProjectEmployees(Integer projectId);

    ProjectMember[] getCompanyEmployees();

    SelectItem getClient(Integer clientID);

    ProjectLabourCosts[] getProjectLabourCostsItems(Integer projectId);

    ProjectViewItem getProjectCostItems(Integer projectID);

    ProjectLabourCosts[] getProjectLabourCostsSubItems(Integer workStreamId);

    void saveProjectWageRates(ProjectMember[] members);

    Integer saveCloneProject(CloneProjectItem item) throws NumberExistingException;

    Integer saveProject(ProjectSingleItem item) throws NumberExistingException;

    Integer saveProject(ProjectSingleItem item, boolean fromCsvImport) throws NumberExistingException;

    void deleteProject(Integer projectId);

    KpiTreeInfo[] getProjectEmployeesForView(Integer projectID);

    KpiTreeInfo[] getProjectEmployeesHistory(Integer projectID);

    Integer getDefaultProjectID();

    DashboardTasks[] getProjectResourceLoad(ListingFilterParameter fp, int period);

    DashboardIssues[] getProjectDashboardIssues(ListingFilterParameter fp, int period);

    HistoryListItem[] getProjectNotes(Integer projectID, Integer limit);

    HistoryListItem[] getProjectNotes(Integer projectID, Integer limit, boolean withAllTaskNotes);

    NewsComment saveProjectNoteComments(NewsComment data);

    void deleteProjectNoteComment(Integer commentId);

    NewsComment[] getProjectNoteComments(Integer noteID);

    ListResult<ProjectInvoice> getPurchaseOrderList(Integer projectId, ListingFilterParameter fp);

    ListResult<ProjectInvoice> getPurchaseInvoiceList(Integer projectId, ListingFilterParameter fp);

    ListResult<ProjectExpenseReportsListItem> getExpenseReportList(Integer projectId, ListingFilterParameter fp);

    void indexProjectTasks(Integer projectID);

    NumberData generateProjectNumber(Date date, Integer clientId, Integer objectID);

    Boolean isDateExistsInNumbering();

    Boolean isClientExistsInNumbering();

    ListResult<ProjectListItem> getProjectList(ListingFilterParameter filterParameter);

    void saveProjectEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus);

    ProjectEmployeeWageClientHistoryItem[] getProjectEmployeeWageClientHistory(Integer projectEmployeeId);

    void updateProjectEmployeeWageClientHistory(ProjectEmployeeWageClientHistoryItem[] hist, Integer projectEmployeeId, Integer projectId);

    void deleteProjectEmployeeWageClientRateHistory(Integer historyId);

    void calculateProjectBudgets(Integer objectID, Boolean isClearAndReCalculate);

    EditProject getProjectDetailsFrom(String projectFrom, Integer projectFromID);

    SelectItem[] getParentIsNullProjects(Integer projectid);

    ProjectBudget getProjectBudgetItems(Integer projectID, boolean withTax);

    ProjectBudget getEmployeeCostClientCharge(Integer projectID);

    SelectItem[] getTimeSheetClients();

    ListResult<BookingItemsItem> getBookingItems(ListingFilterParameter filterParameter);

    BookingItemsItem getBookingItemsData(Integer bookingItemsId);

    BookingReservationItem getBookingItemsReservationData(Integer reservationItem);

    ListResult<BookingReservationItem> getBookingItemsReservationHistory(Integer bookingItemId);

    ArrayList<BookingReservationItem> getBookingItemsReservationHistoryList(Integer bookingItemId);

    Integer saveBookingItem(BookingItemsItem item);

    Integer saveBookingItemReservation(BookingReservationItem item);

    Integer validateBookingItemReservation(BookingReservationItem item);

    NumberData generateBookingItemNumber();

    Date[] getProjectPeriod(Integer projectID);

    NewProjectBudgetData getNewProjectBudgetData(Integer projectID, DateNonConvertable startDate, DateNonConvertable endDate);

    NewProjectBudgetRowItem getProjectBudgetRowDataByAccount(Integer projectID, Integer accountID, ArrayList<DateNonConvertable[]> monthIntervalsList, String type);

    void saveProjectBudgetData(NewProjectBudgetData budgetData);

    SelectItem[] getAccountsForProjectBudget(ListingFilterParameter filterParametrs);

    ListResult<ProjectInvoice> getSaleQuoteList(Integer projectId, ListingFilterParameter fp);

    HashSet<String> getProjectSpecificPermissions(Integer projectID);

    SelectItem getCalendarEventById(Integer clientID);

    SelectItem[] getBookingItemsByCategoryId(Integer viewAs);

    BookingReservationItem getBookingItemReservation(Integer reservationID);

    Boolean isProjectNumberExists(String numberString, Integer projectID);

    void deleteReservation(Integer attachmentId);

    void updateProjectStatus(HashSet<ProjectListItem> projectListItems, SelectItem status, boolean updateProjectStatus);

    ProjectPosition[] getProjectPositions(Integer projectID);

    ListResult<ContractListItem> getContractList(ListingFilterParameter filterParameter);

    void deleteContract(Integer objectId);

    Integer saveContract(ContractSingleItem item);

    void updateContract(EditContract contract);

    EditContract getContractForEdit(Integer contractId);

    ContractViewItem viewContract(Integer objectID);

    TaskTimeEntriesItem[] getProjectTimesheets(Integer projectID);

    SelectItem[] getProjectClients(Integer projectID);

    void deleteBookingItem(Integer id);

    SelectItem getProjectAsLookupItem(Integer id);

    HashSet<SelectItem> getManagers();

    BillOfMaterialItem[] fillBillOfMaterialItemsWithInventory(Integer projectID);

    Integer saveBillOfMaterialItems(Integer projectID, String status, BillOfMaterialItem[] item, String message);

    String[] getBillOfItemsStatus(Integer projectID);

    String unfreezeBOM(Integer projectID);

    Integer saveRequestedBillOfMaterial(Integer projectID, ArrayList<BillOfMaterialItem> items);

    ArrayList<SelectItem> getReferenceByCode(String referenceCode);

    List<NearbyProjectDto> getNearbyProjects(Double latitude, Double longitude, Integer radius, List<Integer> assigneeIds, List<String> statusCodes, List<String> excludeStatusCodes);


    class App {
        public static ProjectServiceAsync get() {
            ServiceDefTarget target = GWT.create(ProjectService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/project");
            return (ProjectServiceAsync) target;
        }
    }

}
