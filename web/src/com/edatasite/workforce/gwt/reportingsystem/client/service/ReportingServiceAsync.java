package com.edatasite.workforce.gwt.reportingsystem.client.service;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.MailListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.RepRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDirectoryPathRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.TelegramRecurrenceMessage;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Virus on 9/11/14.
 */
public interface ReportingServiceAsync {

    void queryForReportsByCategory(Integer categoryId, AsyncCallback<RepRpc> callback);

    void getCategories(AsyncCallback<ArrayList<ReportingCategoryRPC>> callback);

    void getReportList(ListingFilterParameter filter, AsyncCallback<ListResult<SelectListRpc>> callback);

    void getTableColumns(ReportRpc report, AsyncCallback<ArrayList<TableRpc>> callback);

    void getReport(Integer id, AsyncCallback<ReportRpc> asyncCallback);

    void getReport(String uuid, AsyncCallback<ReportRpc> asyncCallback);

    void getReportStructure(ReportRpc report, Integer userId, AsyncCallback<ReportRpc> async);
    void generateReport(ReportRpc report, AsyncCallback<String> async);

    void deleteReport(Integer id, AsyncCallback<Boolean> asyncCallback);

    void getFolders(Integer categoryId, AsyncCallback<FolderRpc[]> abstractAsyncCallback);

    void searchFolders(String search, AsyncCallback<FolderRpc[]> abstractAsyncCallback);

    void saveFolder(FolderRpc rpc, AsyncCallback<Boolean> callback);

    void deleteFolder(Integer id, AsyncCallback<Boolean> callback);

    void getFolder(Integer id, AsyncCallback<FolderRpc> callback);


    void getFilterSelectItems(String searchKey, ColumnRpc columnRpc, ReportRpc report, AsyncCallback<SelectItem[]> callback);

    void createFavouriteReportTemplate(Integer id, AsyncCallback<Boolean> callback);

    void getReportResult(ReportRpc report, AsyncCallback<ReportRpc> callback);

    void getReportChartData(ReportRpc report, boolean isFromRefresh, AsyncCallback<ChartData> callback);

    void getReportChartDataForAi(String uuid, ChartTypeEnum chartType, AsyncCallback<ChartData> callback);

    void saveReport(ReportRpc report, AsyncCallback<Integer> callback);

    void getReportTemplateList(ListingFilterParameter filter, AsyncCallback<ArrayList<ReportDirectoryPathRpc>> callback);

    void getReportStructure(Integer xmlTemplateId, AsyncCallback<ReportRpc> callback);

    void getEmployeeIDsByReportID(Integer id, AsyncCallback<ArrayList<Integer>> callback);

    void changeFolderOfReport(Integer companyID, Integer reportId, String folderName, AsyncCallback<Void> callback);

    void getReportingTopMenu(AsyncCallback<LinkedHashMap<String, LinkedHashMap<String, SelectItem>>> callback);

    void saveMailList(MailListRpc item, ReportRpc report, AsyncCallback<Boolean> callback);

    void addOrRemoveProject(Integer reportId, boolean addProject, AsyncCallback<String> callback);

    void getQueryTotalResult(ReportRpc report, Integer userId, AsyncCallback<ReportRpc> callback);

    void lookupForReportItems(ListingFilterParameter filter, AsyncCallback<ArrayList<SelectItem>> callback);

    void getKpiWidgetData(ReportRpc report, boolean isFromRefresh, AsyncCallback<KpiWidgetData> callback);

    void deleteReportingXMLTemplateFromCompany(Integer templateId, AsyncCallback<Void> voidAsyncCallback);

    void createReportXmlTemplateFromFile(ImportFile importFile, AsyncCallback<Integer> callback);

    void deleteTelegramReportingRecurrenceRule(Integer ruleId, AsyncCallback<ArrayList<String>> integerAsyncCallback);

    void saveTelegramReportingRecurrence(TelegramRecurrenceMessage telegramRecurrenceMessage, boolean shouldRuleBeUpdated, AsyncCallback<ArrayList<String>> integerAsyncCallback);

    void getRuleByName(Integer reportId, String ruleName, AsyncCallback<TelegramRecurrenceMessage> integerAsyncCallback);

    void getReportType(Integer reportId, AsyncCallback<String> stringAsyncCallback);

    void getGroupColumnByReportCodeList(String reportCode, AsyncCallback<List<SelectItem>> async);

    void saveNewGroupColumns(SelectItem selectItem, AsyncCallback<SelectItem> async);

    void searchReport(String searchText, AsyncCallback<SelectListRpc[]> async);

    void saveReportGroups(String reportName, LinkedHashMap<String, String> nameListByPanel, AsyncCallback<Void> async);
}
