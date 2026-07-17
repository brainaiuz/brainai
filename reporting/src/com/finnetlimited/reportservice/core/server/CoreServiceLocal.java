package com.finnetlimited.reportservice.core.server;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.reporting.EdsChartConfig;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCustomizeFilter;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SearchPeopleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.reporting.ReportData;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 5/4/12
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CoreServiceLocal {
    void exportSavedReport(Integer schema, ArrayList<EdsReport> reports, HashMap<Integer, EdsChartConfig> chartHashMap, HashMap<Integer, EdsUpload> uploadHashMap, HashMap<Integer, EdsUploadSettings> uploadSettingsHashMap, HashMap<Integer, EdsKpiWidget> kpiWidgetMap, boolean withPermission);

    ResultSet getSummaryReportResult(ReportRpc report, Integer userId);

    ResultSet getSummaryReportResult(ReportRpc report, Integer userId, boolean isForExport);

    ResultSet getTabularReportResult(ReportRpc report, Integer userId);

    ResultSet getTabularReportResult(ReportRpc report, Integer userId, boolean isForExport);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    ResultSet getTabularReportResult(ReportRpc report, Integer userId, boolean isForExport, DynamicDto customReplacements);

    void exportReportTemplates(LinkedHashMap<String, EdsReportTemplate> map);

    void deleteRolePermission(String code);

    ReportingCustomizeFilter getCustomizeFilter(ReportRpc reportRpc);

    void saveReportPermission(EdsReport report);

    Integer saveOrUpdateReportTemplate(ListingFilterParameter filterParameter);

    ArrayList<SelectItem> getTemplateRoles(Integer companyIDs, Integer objectID);

    void deleteOnboardingReportTemplate(String code, Integer companyID);

    ListResult<SearchPeopleTO> searchPeople(ListingFilterParameter filterParameter);

    SelectItem[] dynamicLookUpResult(String query, String searchKey, Integer limit);

    ArrayList<RoleListItem> getCompanyRoles();

    ArrayList<RejectedImportRecord[]> importReportDataFromCSV(EdsAttachment attachment, ImportFile importFile, List<String[]> listOfRows);

    ArrayList<RejectedImportRecord[]> importReportDataExcel(EdsAttachment attachment, ImportFile importFile, InputStream inputStream);

    Integer createReportXmlTemplate(ImportFile importFile);

    ReportData getReportDateForApi(MListingFilterParameter filterParameter);

    ReportRpc getReport(Integer id, boolean isRecurrence, EdsUser user);

    void setDailyRateRate();

}
