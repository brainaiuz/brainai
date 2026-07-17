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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Virus on 9/11/14.
 */
public interface ReportingService extends RemoteService {

    RepRpc queryForReportsByCategory(Integer categoryIdInteger);

    ArrayList<ReportingCategoryRPC> getCategories();

    ListResult<SelectListRpc> getReportList(ListingFilterParameter filter);

//    ArrayList<ReportingCategoryRPC> getReports();

    ArrayList<TableRpc> getTableColumns(ReportRpc report);

    ReportRpc getReport(Integer id);

    ReportRpc getReport(String uuid);

    ReportRpc getReportStructure(ReportRpc report, Integer userId);
    String generateReport(ReportRpc report);

    ReportRpc getQueryTotalResult(ReportRpc report, Integer userId);

    boolean deleteReport(Integer id);

    FolderRpc[] getFolders(Integer categoryId);

    FolderRpc[] searchFolders(String searchKey);

    Boolean saveFolder(FolderRpc rpc);

    Boolean deleteFolder(Integer id);

    FolderRpc getFolder(Integer id);

    SelectListRpc[] searchReport(String searchText);

    SelectItem[] getFilterSelectItems(String searchKey, ColumnRpc columnRpc, ReportRpc report);

    Boolean createFavouriteReportTemplate(Integer id);

    ReportRpc getReportResult(ReportRpc report);

    ChartData getReportChartData(ReportRpc report, boolean isFromRefresh);

    ChartData getReportChartDataForAi(String uuid, ChartTypeEnum chartType);

    Integer saveReport(ReportRpc report);

    ArrayList<ReportDirectoryPathRpc> getReportTemplateList(ListingFilterParameter filter);

    ReportRpc getReportStructure(Integer xmlTemplateId);

    ArrayList<Integer> getEmployeeIDsByReportID(Integer id);

    void changeFolderOfReport(Integer companyID, Integer reportId, String folderName);

    LinkedHashMap<String, LinkedHashMap<String, SelectItem>> getReportingTopMenu();

    Boolean saveMailList(MailListRpc item, ReportRpc report);

    String addOrRemoveProject(Integer reportId, boolean addProject);

    ArrayList<SelectItem> lookupForReportItems(ListingFilterParameter filter);

    KpiWidgetData getKpiWidgetData(ReportRpc report, boolean isFromRefresh);

    void deleteReportingXMLTemplateFromCompany(Integer templateId);

    Integer createReportXmlTemplateFromFile(ImportFile importFile);

    ArrayList<String> deleteTelegramReportingRecurrenceRule(Integer ruleId);

    ArrayList<String> saveTelegramReportingRecurrence(TelegramRecurrenceMessage telegramRecurrenceMessage, boolean shouldRuleBeUpdated);

    TelegramRecurrenceMessage getRuleByName(Integer reportId, String ruleName);

    String getReportType(Integer reportId);


    List<SelectItem> getGroupColumnByReportCodeList(String reportCode);

    SelectItem saveNewGroupColumns(SelectItem selectItem);

    void saveReportGroups(String reportName, LinkedHashMap<String, String> nameListByPanel);

    class App {
        public static ReportingServiceAsync get() {
            ServiceDefTarget target = GWT.create(ReportingService.class);
            target.setServiceEntryPoint("/rpc/reportingService");
            return (ReportingServiceAsync) target;
        }
    }
}
