package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.*;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Dilsh0d
 * Date: 04-Mar-2010
 * Time: 16:21:24
 */
public interface CoreServiceAsync {

    void saveFolder(FolderRpc folder, AsyncCallback<Boolean> callback);

    void updateFolder(FolderRpc folder, AsyncCallback<Boolean> callback);

    void saveReport(ReportRpc report, AsyncCallback<Integer> callback);

    void deleteReport(Integer id, AsyncCallback<Boolean> callback);

    void deleteFolder(Integer id, AsyncCallback<Boolean> callback);

    void getReport(Integer id, AsyncCallback<ReportRpc> callback);

    void getReport(Integer id, boolean recurrence, AsyncCallback<ReportRpc> callback);

    void getFolder(Integer id, AsyncCallback<FolderRpc> callback);

    void getFolderByReportId(Integer id, AsyncCallback<FolderRpc> callback);

    void getFolderList(AsyncCallback<ArrayList<SelectListRpc>> callback);

    void getReportTemplateList(ListingFilterParameter filter, AsyncCallback<ArrayList<ReportDirectoryPathRpc>> callback);

    void getUser(AsyncCallback<UserSecuritryRpc> callback);

    void getUser(Integer userId, AsyncCallback<UserSecuritryRpc> callback);

    void getCustomItems(String query, AsyncCallback<ArrayList<SelectItem>> callback);

    void getReportStructure(String viewCode, AsyncCallback<ViewRpc> callback);

    void getReportListByUser(AsyncCallback<ArrayList<FolderRpc>> callback);

    void getTableColumns(ReportRpc report, AsyncCallback<ArrayList<TableRpc>> callback);

    void getSummariesColumns(ReportRpc report, AsyncCallback<ArrayList<ColumnRpc>> callback);

    void getSelectedColumns(ReportRpc report, AsyncCallback<LinkedList<ColumnRpc>> callback);

    void getFilterColumns(ReportRpc report, AsyncCallback<LinkedList<ColumnRpc>> callback);

    void getReportResult(ReportRpc report, AsyncCallback<ReportGenerateTableRpc> callback);

    void getReportChartData(ReportRpc report, boolean isFromRefresh, AsyncCallback<ChartData> callback);

//    void getReportResult(ReportRpc report, Integer userId, AsyncCallback<ReportGenerateTableRpc> callback);

    void getUserNameAndCompanyName(AsyncCallback<String[]> callback);

    void getFolderTypeByReportId(Integer id, AsyncCallback<String> callback);

    void getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column, AsyncCallback<LinkedList<SelectItem>> async);

    void getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column, boolean fullSearchKey, AsyncCallback<LinkedList<SelectItem>> async);

    void getThemeForSystem(AsyncCallback<String> async);

    Request getReportingTemplateList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ReportingListItem>> callback);

    void updateReportTemplate(ReportingListItem report, AsyncCallback<Boolean> async);

    void sendToClient(Integer reportID, Integer userId, Integer companyId, String category, Integer recurrenceId, AsyncCallback<Void> async);

    void saveReport(ReportRpc report, Integer companyId, WorkflowTelegramAlert telegramAlert, AsyncCallback<Integer> async);

    void getReport(Integer id, Integer companyId, AsyncCallback<ReportRpc> async);

    void saveOrUpdateReportTemplate(ListingFilterParameter filterParameter, AsyncCallback<Integer> callback);

    void getReportTemplate(Integer objectID, AsyncCallback<ReportTemplateItem> callback);

    void getReportingXMLTemplateList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ReportingListItem>> callback);

    void deleteReportingXMLTemplateFromCompany(Integer objectID, Integer companyID, AsyncCallback<Void> callback);

    void getCompanies(Integer templateID, AsyncCallback<ArrayList<TeamEmployees>> callback);

    void saveOrUpdateReportTemplateCategory(com.edatasite.workforce.gwt.core.client.rpc.SelectItem categoryItem, AsyncCallback<Void> callback);

    void getReportTemplateCategory(Integer objectID, AsyncCallback<SelectItem> callback);

    void getReportTemplateCategories(AsyncCallback<ArrayList<SelectListRpc>> callback);

    void getEmployeeIDsByReportID(Integer reportID, AsyncCallback<ArrayList<Integer>> callback);

    void getReport(Integer id, Integer companyID, Integer userID, AsyncCallback<ReportRpc> callback);

    void makeTestingReportSchema(Integer mySchema, AsyncCallback callback);

    void executeNative(String query, AsyncCallback callback);

    void getDataTable(String sqlQuery, AsyncCallback<ArrayList<String[]>> callback);

    void getValue(String sqlQuery, AsyncCallback<String> callback);

    void getReportsNative(ListingFilterParameter filterParametrs, AsyncCallback<ArrayList<String[]>> callback);

    void getReportsNativeCount(ListingFilterParameter filterParametrs, AsyncCallback<Integer> callback);

    void runReport(ArrayList<Integer> integers, Integer companyid, AsyncCallback callback);

    void runSingleReport(Integer id, Integer companyID, AsyncCallback callback);

    void getReportListUser(AsyncCallback<ArrayList<ReportTemplateCategoryRpc>> asyncCallback);

    void createFavouriteReportTemplate(Integer reportinhid, AsyncCallback<Boolean> callback);

    void getFavReports(AsyncCallback<ArrayList<ListItem>> callback);

    void getUserReportList(Integer reportid, Boolean islibrary, AsyncCallback<LinkedList<SelectItem>> callback);

    void getReportStar(Integer reportid, AsyncCallback<Boolean> callback);

    void saveReportTemplate(ReportingListItem rowValue, AsyncCallback<Void> asyncCallback);

    void getReportDBUrlList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ReportingDBUrlListItem>> asyncCallback);

    void getReportingDBUrlCompanies(Integer objectID, AsyncCallback<ArrayList<TeamEmployees>> asyncCallback);

    void getReportingDBUrl(Integer objectID, AsyncCallback<ReportingDBUrlListItem> asyncCallback);

    void saveReportingDBUrl(ReportingDBUrlListItem item, AsyncCallback<Void> asyncCallback);

    void deleteReportingDBUrl(Integer id, AsyncCallback<Void> asyncCallback);

    void getCompanyReportList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SelectListRpc>> asyncCallback);

    void getSavedReportInsertQuery(ListingFilterParameter filterParameter, AsyncCallback<String> asyncCallback);

    void updateSavedReport(ListingFilterParameter filterParametrs, AsyncCallback<String> asyncCallback);

    void getSavedReportUpdateCommand(ListingFilterParameter filterParametrs, AsyncCallback<String> asyncCallback);

    void deleteReportsByCompany(ListingFilterParameter filterParametrs, AsyncCallback<Void> asyncCallback);

    void getInsertCommand(Integer[] IDs, AsyncCallback<String> asyncCallback);

    void getTemplateRoles(Integer companyIDs, Integer objectID, AsyncCallback<ArrayList<SelectItem>> asyncCallback);

    void saveMailList(MailListRpc item, ReportRpc report, AsyncCallback<Boolean> asyncCallback);

    void getCompanyRoles(AsyncCallback<ArrayList<RoleListItem>> callback);

    void getReportStructure(ReportRpc report, Integer userId, AsyncCallback<ReportRpc> async);

    void getReportTemplates(ListingFilterParameter filterParams, AsyncCallback<SelectItem[]> async);

    void savedReportChange(SavedReportTemplate item, AsyncCallback<SavedReportTemplate> asyncCallback);

    void isComplate(String typeString, Integer entityID, AsyncCallback<Boolean> callback);

    void getRelatedLinks(Integer objectId, String viewType, AsyncCallback<ArrayList<RelatedLinkRPC>> callback);

    void getCategoriesForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<PaymentDeductionSelectItem[]> callback);

    void getQueryTotalResult(ReportRpc report, Integer userId, AsyncCallback<ReportRpc> async);

    void getCashAdvanceList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CashAdvanceItem>> callback);

    void deleteCashAdvance(Integer objectId, AsyncCallback<Boolean> callback);

    void getCashAdvanceUpdates(Integer objectId, AsyncCallback<ArrayList<MyUpdateItem>> callback);

    void getCashAdvancedItem(ListingFilterParameter fp, AsyncCallback<CashAdvanceItem> async);

    void saveCashAdvance(CashAdvanceItem cashAdvanceItem, AsyncCallback<TestRPC> async);

    void getDriversForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void saveCashAdvancePayment(CashAdvancePayment payment, AsyncCallback<TestRPC> callback);

    void getCashAdvancePayments(ListingFilterParameter filter, AsyncCallback<ListResult<CashAdvancePayment>> callback);

    void deleteCashAdvancePayment(Integer cashAdvanceId, Integer paymentId, AsyncCallback<TestRPC> callback);

    void getKpiWidgetData(ReportRpc report, boolean isFromRefresh, AsyncCallback<KpiWidgetData> callback);

    void createXmlBackupFile(AsyncCallback<Void> callback);

    void getFolderResource(int folderType, Integer entityID, AsyncCallback<FolderResource> async);

    void saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String description, AsyncCallback<ArrayList<FileResource>> async);

    void createFolder(Integer parentId, String name, AsyncCallback<FolderResource> async);

    void createOrUpdateCustomHtml(String text, Integer reportId, AsyncCallback<String> callback);
    void  getCustomHtmlCodeByReportId(Integer id, AsyncCallback<String> html);
    void getDefaultHtmlCode(AsyncCallback<String> htmlCode);
}
