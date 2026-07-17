package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IssueListExcelHandler extends BaseExcelHandler {

    @Autowired
    private IssueService issueService;
    @Autowired
    private PropertManager propertManager;

    private static final Logger log = LoggerFactory.getLogger(IssueListExcelHandler.class.getName());
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "";
        EdsUser user = userManager.getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_IssueList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        String sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.issues);
        fp.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ListResult<IssueListItem> issueListItemListResult = issueService.getIssuesList(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        log.info("Start excel generation data");
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(IssueListItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put("relatedTo", new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedTo), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.RESOLVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.resolverOwner), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.TIMESHEET, new ExcelData(commonLocalizer.localize(PdfLocalizationName.timesheet), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(IssueListItem.PRIORITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.priority), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(RelationItem.TYPE_CONTACT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_LEAD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToCRMAccount), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CASE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedCase), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EVENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_TASK, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedTask), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToProject), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (IssueListItem item : issueListItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(IssueListItem.NUMBER)) {
                    mapColumns.put(IssueListItem.NUMBER, new ExcelData(item.getNumber() != null ? item.getNumber() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(IssueListItem.NAME)) {
                    mapColumns.put(IssueListItem.NAME, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(IssueListItem.DESCRIPTION)) {
                    mapColumns.put(IssueListItem.DESCRIPTION, new ExcelData(item.getDescription() != null ? item.getDescription() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains("relatedTo")) {
                    mapColumns.put("relatedTo", new ExcelData(item.getRelatedTo() != null ? item.getRelatedTo() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(IssueListItem.PERIOD)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumns.put(IssueListItem.PERIOD, new ExcelData(item.getStartDate() != null && item.getEndDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate())) + " - " + ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())) : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumns.put(IssueListItem.PERIOD, new ExcelData(item.getStartDate() != null && item.getEndDate() != null ? dateFormat(item.getStartDate()) + " - " + dateFormat(item.getEndDate()) : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }
                if (panelTools.getColumnCodeName().contains(IssueListItem.STATUS)) {
                    mapColumns.put(IssueListItem.STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(IssueListItem.RESOLVER)) {
                    mapColumns.put(IssueListItem.RESOLVER, new ExcelData(item.getResolver() != null ? item.getResolver() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(IssueListItem.TIMESHEET)) {
                    mapColumns.put(IssueListItem.TIMESHEET, new ExcelData(item.isTimeSheetEnabled() ? commonLocalizer.localize(PdfLocalizationName.enabled) : commonLocalizer.localize(PdfLocalizationName.disabled), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(IssueListItem.PRIORITY)) {
                    mapColumns.put(IssueListItem.PRIORITY, new ExcelData(item.getPriority() != null ? item.getPriority() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related contact
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                    mapColumns.put(RelationItem.TYPE_CONTACT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related lead
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                    mapColumns.put(RelationItem.TYPE_LEAD, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related crm account
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                    mapColumns.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related case
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    mapColumns.put(RelationItem.TYPE_CASE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related opportunity
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                    mapColumns.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related event
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                    mapColumns.put(RelationItem.TYPE_EVENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related task
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                    mapColumns.put(RelationItem.TYPE_TASK, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related project
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                    mapColumns.put(RelationItem.TYPE_PROJECT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related employee
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                    mapColumns.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related department
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                    mapColumns.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related client
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
                    mapColumns.put(RelationItem.TYPE_CLIENT, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related supplier
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                    mapColumns.put(RelationItem.TYPE_SUPPLIER, new ExcelData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany);

                excelDataList = new ArrayList<>();
                for (String columnName : panelTools.getColumnCodeName()) {
                    if (mapColumns.containsKey(columnName)) {
                        excelDataList.add(mapColumns.get(columnName));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            //WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Issue list excel report, exception: " + e);
        }
        return null;
    }

    private String dateFormat(Date date) {
        return ServerUtils.shortDateFormat(date, userManager.getUser());
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}