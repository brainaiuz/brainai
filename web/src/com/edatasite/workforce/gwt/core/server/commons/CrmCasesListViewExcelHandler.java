package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 */
public class CrmCasesListViewExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmCasesListViewExcelHandler.class);

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private CRMService crmService;
    @Autowired
    private UserManager userManager;

    @Override
    protected void setFileName() {
        filename = "Crm Cases";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        long startedAt = System.currentTimeMillis();
        System.out.println("Export to excel started at:===========================" + new Date() + "===========================");
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(false);
        EdsCompany edsCompany = userManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        long caseStartedAt = System.currentTimeMillis();
        System.out.println("Getting Case List started at:===========================" + new Date() + "===========================");
        CaseList caseList = crmService.getCases(filterParametrs);
        System.out.println("It took to get Case List:===========================" + (System.currentTimeMillis() - caseStartedAt) + "===========================");
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CaseItem> caseListItems = caseList.getList();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = getMapColumnHeader();
        List<ExcelData[]> list = new LinkedList<>();
        try {
            //"Case Number", "Subject", "Priority", "Reported By", "Created Date", "Assigned To", "Status"
            if (panelTools.isCustomFieldsShown()) {
                CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            }

            // Set excell header
            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            long insertRawValuesStartedAt = System.currentTimeMillis();
            System.out.println("Insert column values started at:===========================" + new Date() + "===========================");
            for (CaseItem item : caseListItems) {
                Map<String, ExcelData> mapColumn = getMapColumnValues(panelTools, item);

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, edsCompany);

                excellDatasList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excellDatasList.add(getExcelRows(mapColumn.get(panelTools.getColumnCodeName().get(i))));
                    }
                }
                cellDatas = new ExcelData[excellDatasList.size()];
                excellDatasList.toArray(cellDatas);
                list.add(cellDatas);
            }
            System.out.println("It took to insert values into Case List:===========================" + (System.currentTimeMillis() - insertRawValuesStartedAt) + "===========================");
            System.out.println("Overall time is:===========================" + (System.currentTimeMillis() - startedAt) + "===========================");
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Crm cases list excel report, exception: " + e);
        }
        return null;
    }

    private Map<String, ExcelData> getMapColumnHeader() {
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CaseItem.CASE_ID, new ExcelData(crmLocalizer.localize(PdfLocalizationName.caseID), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.SUBJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.subject), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.PRIORITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.priority), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.REPORTED_BY, new ExcelData(crmLocalizer.localize(PdfLocalizationName.reportedBy), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.CREATED_DATE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.LAST_UPDATED_DATE, new ExcelData(crmLocalizer.localize(PdfLocalizationName.lastUpdated), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.ASSIGNED_TO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assignedTo), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.RESOLVER, new ExcelData(crmLocalizer.localize(PdfLocalizationName.resolver), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.CASE_REASON, new ExcelData(commonLocalizer.localize(PdfLocalizationName.caseReason), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.BILLABLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billable), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.CASE_TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(CaseItem.ORIGIN, new ExcelData(commonLocalizer.localize(PdfLocalizationName.origin), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_CONTACT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_LEAD, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedLead), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedCRMAccount), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedOpportunity), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_TASK, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedTask), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_EVENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_PROJECT, new ExcelData(crmLocalizer.localize(PdfLocalizationName.relatedProject), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

        mapColumnHeader.put(RelationItem.TYPE_ISSUE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

        return mapColumnHeader;
    }

    private Map<String, ExcelData> getMapColumnValues(ListPanelToolRpc panelTools, CaseItem item) {
        Map<String, ExcelData> mapColumn = new HashMap<>();

        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_ID)) {
            mapColumn.put(CaseItem.CASE_ID, new ExcelData(item.getCaseNumber(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.SUBJECT)) {
            mapColumn.put(CaseItem.SUBJECT, new ExcelData(item.getSubject() != null ? item.getSubject() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.PRIORITY)) {
            mapColumn.put(CaseItem.PRIORITY, new ExcelData(item.getPriority() != null ? item.getPriority() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.REPORTED_BY)) {
            mapColumn.put(CaseItem.REPORTED_BY, new ExcelData(item.getReportedBy() != null ? item.getReportedBy() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.CREATED_DATE)) {
            mapColumn.put(CaseItem.CREATED_DATE, new ExcelData((ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getCreatedDate() != null ? item.getCreatedDate() : new Date())) : dateFormat(item.getCreatedDate() != null ? item.getCreatedDate() : new Date())), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.LAST_UPDATED_DATE)) {
            mapColumn.put(CaseItem.LAST_UPDATED_DATE, new ExcelData((ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getLastUpdatedDate() != null ? item.getLastUpdatedDate() : new Date())) : dateFormat(item.getLastUpdatedDate() != null ? item.getLastUpdatedDate() : new Date())), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.ASSIGNED_TO)) {
            mapColumn.put(CaseItem.ASSIGNED_TO, new ExcelData(item.getCaseAssigneeName() != null ? item.getCaseAssigneeName() : item.getDepartment() == null ? "" : item.getDepartment(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.RESOLVER)) {
            mapColumn.put(CaseItem.RESOLVER, new ExcelData(item.getResolverName() != null ? item.getResolverName() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.STATUS)) {
            mapColumn.put(CaseItem.STATUS, new ExcelData(item.getStatus().getId() != null ? item.getStatus().getName() : "N/A", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_REASON)) {
            mapColumn.put(CaseItem.CASE_REASON, new ExcelData(item.getCaseReason() != null ? item.getCaseReason() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_TYPE)) {
            mapColumn.put(CaseItem.CASE_TYPE, new ExcelData(item.getType() != null ? item.getType() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.ORIGIN)) {
            mapColumn.put(CaseItem.ORIGIN, new ExcelData(item.getCaseOrigin() != null ? item.getCaseOrigin() : "", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
            mapColumn.put(RelationItem.TYPE_CONTACT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
            mapColumn.put(RelationItem.TYPE_LEAD, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
            mapColumn.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
            mapColumn.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
            mapColumn.put(RelationItem.TYPE_TASK, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
            mapColumn.put(RelationItem.TYPE_EVENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
            mapColumn.put(RelationItem.TYPE_PROJECT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        //related issue
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
            mapColumn.put(RelationItem.TYPE_ISSUE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_ISSUE), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        //related employee
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
            mapColumn.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        //related department
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
            mapColumn.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        //related client
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
            mapColumn.put(RelationItem.TYPE_CLIENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }
        //related supplier
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
            mapColumn.put(RelationItem.TYPE_SUPPLIER, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
        }

        if (panelTools.isCustomFieldsShown()) {
            for (String key : item.getCustomFieldsMap().keySet()) {
                if (item.getCustomFieldsMap().get(key) != null) {
                    if (item.getCustomFieldsMap().get(key) instanceof Date) {
                        mapColumn.put(key, new ExcelData(dateFormat((Date) item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else if (item.getCustomFieldsMap().get(key) instanceof Double) {
                        mapColumn.put(key, new ExcelData(NumberFormat.getNumberInstance().format(item.getCustomFieldsMap().get(key)), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(key, new ExcelData(item.getCustomFieldsMap().get(key).toString(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                } else {
                    mapColumn.put(key, new ExcelData("", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
            }
        }
        return mapColumn;
    }

    protected String dateFormat(Date date, boolean... isServerTime) {
        return ServerUtils.longDateFormat(date, userManager.getUser(), isServerTime == null || isServerTime.length <= 0 || !isServerTime[0]);
    }
}