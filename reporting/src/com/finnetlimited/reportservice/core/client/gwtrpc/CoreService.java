package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Dilsh0d
 * Date: 04-Mar-2010
 * Time: 16:21:24
 */
public interface CoreService extends RemoteService {

    boolean saveFolder(FolderRpc folder);

    boolean updateFolder(FolderRpc folder);

    Integer saveReport(ReportRpc report);

    Integer saveReport(ReportRpc report, Integer companyId, WorkflowTelegramAlert telegramAlert);

    boolean deleteReport(Integer id);

    boolean updateReportTemplate(ReportingListItem report);

    boolean deleteFolder(Integer id);

    ReportRpc getReport(Integer id);

    ReportRpc getReport(Integer id, Integer schemaCompanyId);

    ReportRpc getReport(Integer id, boolean recurrence);

    FolderRpc getFolder(Integer id);

    FolderRpc getFolderByReportId(Integer id);

    ArrayList<SelectListRpc> getFolderList();

    ArrayList<ReportDirectoryPathRpc> getReportTemplateList(ListingFilterParameter filter);

    UserSecuritryRpc getUser();

    UserSecuritryRpc getUser(Integer userId);

    String getThemeForSystem();

    ArrayList<FolderRpc> getReportListByUser();

    ArrayList<SelectItem> getCustomItems(String query);

    ArrayList<TableRpc> getTableColumns(ReportRpc report);

    ArrayList<ColumnRpc> getSummariesColumns(ReportRpc report);

    LinkedList<ColumnRpc> getSelectedColumns(ReportRpc report);

    LinkedList<ColumnRpc> getFilterColumns(ReportRpc report);

    ReportGenerateTableRpc getReportResult(ReportRpc report);

    ChartData getReportChartData(ReportRpc report, boolean isFromRefresh);

//    ReportGenerateTableRpc getReportResult(ReportRpc report, Integer userId);

    String[] getUserNameAndCompanyName();

    String getFolderTypeByReportId(Integer id);

    LinkedList<SelectItem> getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column);

    LinkedList<SelectItem> getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column, boolean fullSearchKey);

    ListResult<ReportingListItem> getReportingTemplateList(ListingFilterParameter filterParametrs);

    void sendToClient(Integer reportID, Integer userId, Integer companyId, String category, Integer recurrenceId);

    Integer saveOrUpdateReportTemplate(ListingFilterParameter filterParameter);

    ReportTemplateItem getReportTemplate(Integer objectID);

    ListResult<ReportingListItem> getReportingXMLTemplateList(ListingFilterParameter filterParameter);

    void deleteReportingXMLTemplateFromCompany(Integer objectID, Integer companyID);

    ArrayList<TeamEmployees> getCompanies(Integer templateID);

    void saveOrUpdateReportTemplateCategory(SelectItem categoryItem);

    SelectItem getReportTemplateCategory(Integer objectID);

    ArrayList<SelectListRpc> getReportTemplateCategories();

    ArrayList<Integer> getEmployeeIDsByReportID(Integer reportID);

    ReportRpc getReport(Integer id, Integer companyID, Integer userID);

    void makeTestingReportSchema(Integer mySchema);

    Boolean executeNative(String query);

    ArrayList<String[]> getDataTable(String sqlQuery);

    String getValue(String sqlQuery);

    ArrayList<String[]> getReportsNative(ListingFilterParameter filterParametrs);

    Integer getReportsNativeCount(ListingFilterParameter filterParametrs);

    void runReport(ArrayList<Integer> integers, Integer companyid);

    void runSingleReport(Integer id, Integer companyID);

    ArrayList<ReportTemplateCategoryRpc> getReportListUser();

    ViewRpc getReportStructure(String viewCode);

    Boolean createFavouriteReportTemplate(Integer reportid);

    ArrayList<ListItem> getFavReports();

    LinkedList<SelectItem> getUserReportList(Integer reportid, Boolean islibrary);

    Boolean getReportStar(Integer reportid);

    void saveReportTemplate(ReportingListItem rowValue);

    ListResult<ReportingDBUrlListItem> getReportDBUrlList(ListingFilterParameter filterParameter);

    ArrayList<TeamEmployees> getReportingDBUrlCompanies(Integer objectID);

    ReportingDBUrlListItem getReportingDBUrl(Integer objectID);

    void saveReportingDBUrl(ReportingDBUrlListItem item);

    void deleteReportingDBUrl(Integer id);

    ListResult<SelectListRpc> getCompanyReportList(ListingFilterParameter filterParameter);

    String getSavedReportInsertQuery(ListingFilterParameter filterParameter);

    String updateSavedReport(ListingFilterParameter filterParametrs);

    String getSavedReportUpdateCommand(ListingFilterParameter filterParametrs);

    void deleteReportsByCompany(ListingFilterParameter filterParametrs);

    String getInsertCommand(Integer[] IDs);

    ArrayList<SelectItem> getTemplateRoles(Integer companyIDs, Integer objectID);

    Boolean saveMailList(MailListRpc item, ReportRpc report);

    ArrayList<RoleListItem> getCompanyRoles();

    ReportRpc getReportStructure(ReportRpc report, Integer userId);

    SelectItem[] getReportTemplates(ListingFilterParameter filterParams);

    SavedReportTemplate savedReportChange(SavedReportTemplate item);

    boolean isComplate(String typeString, Integer entityID);

    ArrayList<RelatedLinkRPC> getRelatedLinks(Integer objectId, String viewType);

    PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs);

    ReportRpc getQueryTotalResult(ReportRpc report, Integer userId);

    ListResult<CashAdvanceItem> getCashAdvanceList(ListingFilterParameter filterParametrs);

    boolean deleteCashAdvance(Integer objectId);

    ArrayList<MyUpdateItem> getCashAdvanceUpdates(Integer objectId);

    CashAdvanceItem getCashAdvancedItem(ListingFilterParameter fp);

    TestRPC saveCashAdvance(CashAdvanceItem cashAdvanceItem);

    SelectItem[] getDriversForLookUp(ListingFilterParameter filterParameter);

    TestRPC saveCashAdvancePayment(CashAdvancePayment payment);

    ListResult<CashAdvancePayment> getCashAdvancePayments(ListingFilterParameter filter);

    TestRPC deleteCashAdvancePayment(Integer cashAdvanceId, Integer paymentId);

    KpiWidgetData getKpiWidgetData(ReportRpc report, boolean isFromRefresh);

    void createXmlBackupFile();

    FolderResource createFolder(Integer parentId, String name) throws InsufficientPermissionsException, DuplicateNameException, ObjectNotFoundException;

    FolderResource getFolderResource(int folderType, Integer entityID);

    ArrayList<FileResource> saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String description);
    String createOrUpdateCustomHtml(String text, Integer reportId);
    String getCustomHtmlCodeByReportId(Integer id);
    String getDefaultHtmlCode();

    /*
     * Utility/Convenience class.
     * Use Core.App.getInstance() to access static instance of CoreAsync
     */
    class App {
        public static CoreServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreService.class);
            target.setServiceEntryPoint("/rpc/reportingCoreService");
            return (CoreServiceAsync) target;
        }
    }
}
