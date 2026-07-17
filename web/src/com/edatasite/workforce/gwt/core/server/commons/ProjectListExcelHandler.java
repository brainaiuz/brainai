package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.lowagie.text.Element;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProjectListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(ProjectListExcelHandler.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = "Project List";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        String sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.projects);
        String shortDateFormat = "MM/dd/yyyy";
        EdsUser user = projectManager.getUser();
        EdsCompany edsCompany = projectManager.getUser().getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        filterParametrs.setLimit(1000);
        ListResult<ProjectListItem> projList = projectService.getProjectList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<ProjectListItem> projects = projList.getList();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnData.put(ProjectListItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.BACKUP_MANAGER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.backupManager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.INCOME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.income), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PROFIT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.profit), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.COST, new ExcelData(commonLocalizer.localize(PdfLocalizationName.cost), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PLANED_PROFIT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.planedProfit), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PLANED_COST, new ExcelData(commonLocalizer.localize(PdfLocalizationName.planedCost), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PLANED_INCOME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.planedIncome), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.ACTUAL_TIME_SPENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.actualTimeSpent), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.HOURS_SPENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.NUMBER_OF_TASKS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.noOfTask), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.HEAD_COUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.headCount), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PERCENT_COMPLETED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.percentCompleted), ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDateField), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDateField), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.ESTIMATED_TIME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.estimatedTime), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.INVOICES, new ExcelData(commonLocalizer.localize("invoices"), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_CONTACT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_LEAD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToCRMAccount), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_CASE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedCase), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_EVENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_TASK, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedTask), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnData.put(RelationItem.TYPE_ISSUE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.PROJECT_RELATION_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnData.put(ProjectListItem.WAITING_HOURS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.waitingForApproval), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.REJECTED_HOURS, new ExcelData(pmLocalizer.localize(PdfLocalizationName.rejectedHours), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.CREATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.CONTRACT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.contract), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.MODIFIED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnData.put(ProjectListItem.MODIFIED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnData);

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnData.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnData.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);
            for (ProjectListItem project : projects) {
                String number = " ";
                String manager = " ";
                String client = " ";
                String status = " ";
                if (project.getNumber() != null) {
                    number = project.getNumber();
                }

                if (project.getManager() != null) {
                    manager = project.getManager();
                }
                if (project.getClient() != null) {
                    client = project.getClient();
                }
                if (project.getStatus() != null) {
                    status = project.getStatus();
                }
                String tasksCount = project.getTaskCount() != null ? project.getTaskCount().toString() : "0";
                String headsCount = project.getHeadCount() != null ? project.getHeadCount().toString() : "0";
                String percentCompleted = project.getComplete() != null ? /*getMoneyFormat(parseToDouble(*/project.getComplete()/*))*/ : "0.00";

                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NUMBER)) {
                    mapColumn.put(ProjectListItem.NUMBER, new ExcelData(number, ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NAME)) {
                    mapColumn.put(ProjectListItem.NAME, new ExcelData(project.getName(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.DESCRIPTION)) {
                    mapColumn.put(ProjectListItem.DESCRIPTION, new ExcelData(project.getDescription(), ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.MANAGER)) {
                    mapColumn.put(ProjectListItem.MANAGER, new ExcelData(manager, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.BACKUP_MANAGER)) {
                    mapColumn.put(ProjectListItem.BACKUP_MANAGER, new ExcelData(project.getBackupManager(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CLIENT)) {
                    mapColumn.put(ProjectListItem.CLIENT, new ExcelData(project.getClient(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.ACTUAL_TIME_SPENT)) {
                    mapColumn.put(ProjectListItem.ACTUAL_TIME_SPENT, new ExcelData(project.getActualHoursSpent(), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.HOURS_SPENT)) {
                    mapColumn.put(ProjectListItem.HOURS_SPENT, new ExcelData(project.getHoursSpent(), ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NUMBER_OF_TASKS)) {
                    mapColumn.put(ProjectListItem.NUMBER_OF_TASKS, new ExcelData(tasksCount, ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.HEAD_COUNT)) {
                    mapColumn.put(ProjectListItem.HEAD_COUNT, new ExcelData(headsCount, ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.STATUS)) {
                    mapColumn.put(ProjectListItem.STATUS, new ExcelData(status, ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PERCENT_COMPLETED)) {
                    mapColumn.put(ProjectListItem.PERCENT_COMPLETED, new ExcelData(percentCompleted, ExcelData.STRING, 15, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.START_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(ProjectListItem.START_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(projectManager.getUser().getUserDate(project.getStartDate()), shortDateFormat)), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(ProjectListItem.START_DATE, new ExcelData(ServerUtils.dateFormat(projectManager.getUser().getUserDate(project.getStartDate()), shortDateFormat), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.END_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(ProjectListItem.END_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(projectManager.getUser().getUserDate(project.getEndDate()), shortDateFormat)), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(ProjectListItem.END_DATE, new ExcelData(ServerUtils.dateFormat(projectManager.getUser().getUserDate(project.getEndDate()), shortDateFormat), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.ESTIMATED_TIME)) {
                    mapColumn.put(ProjectListItem.ESTIMATED_TIME, new ExcelData(project.getEstimatedTime() != null ? formatIntToTime(project.getEstimatedTime()) : "00:00", ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.INVOICES)) {
                    mapColumn.put(ProjectListItem.INVOICES, new ExcelData(project.getInvoiceNumber() != null ? project.getInvoiceNumber() : " ", ExcelData.STRING, 9, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                    mapColumn.put(RelationItem.TYPE_CONTACT, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                    mapColumn.put(RelationItem.TYPE_LEAD, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                    mapColumn.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    mapColumn.put(RelationItem.TYPE_CASE, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_CASE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                    mapColumn.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                    mapColumn.put(RelationItem.TYPE_EVENT, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_EVENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                    mapColumn.put(RelationItem.TYPE_TASK, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_TASK), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related issue
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                    mapColumn.put(RelationItem.TYPE_ISSUE, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_ISSUE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related employee
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                    mapColumn.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related department
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                    mapColumn.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related client
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PROJECT_RELATION_CLIENT)) {
                    mapColumn.put(ProjectListItem.PROJECT_RELATION_CLIENT, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_CLIENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related supplier
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                    mapColumn.put(RelationItem.TYPE_SUPPLIER, new ExcelData(project.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.INCOME)) {
                    mapColumn.put(ProjectListItem.INCOME, new ExcelData(getMoneyFormat(project.getIncome()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //profit
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROFIT)) {
                    mapColumn.put(RelationItem.TYPE_PROFIT, new ExcelData(getMoneyFormat(project.getProfit()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //cost
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_COST)) {
                    mapColumn.put(RelationItem.TYPE_COST, new ExcelData(getMoneyFormat(project.getCost()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //planned_profit
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_PROFIT)) {
                    mapColumn.put(ProjectListItem.PLANED_PROFIT, new ExcelData(getMoneyFormat(project.getPlanedProfit()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //planned_cost
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_COST)) {
                    mapColumn.put(ProjectListItem.PLANED_COST, new ExcelData(getMoneyFormat(project.getPlanedCost()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //planned_cost
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_INCOME)) {
                    mapColumn.put(ProjectListItem.PLANED_INCOME, new ExcelData(getMoneyFormat(project.getPlanedIncome()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.WAITING_HOURS)) {
                    mapColumn.put(ProjectListItem.WAITING_HOURS, new ExcelData(project.getWaitingHours(), ExcelData.STRING, 9, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.REJECTED_HOURS)) {
                    mapColumn.put(ProjectListItem.REJECTED_HOURS, new ExcelData(project.getRejectedHours(), ExcelData.STRING, 9, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CREATED_BY)) {
                    mapColumn.put(ProjectListItem.CREATED_BY, new ExcelData(project.getCreatedBy(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CONTRACT)) {
                    mapColumn.put(ProjectListItem.CONTRACT, new ExcelData(project.getContractName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                // Created date
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CREATED_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(ProjectListItem.CREATED_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(longDateFormat(project.getCreatedDate())), Element.ALIGN_LEFT));
                    } else {
                        mapColumn.put(ProjectListItem.CREATED_DATE, new ExcelData(longDateFormat(project.getCreatedDate()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                // Modified by
                if (panelTools.getColumnCodeName().contains(ProjectListItem.MODIFIED_BY)) {
                    mapColumn.put(ProjectListItem.MODIFIED_BY, new ExcelData(project.getCreatedBy(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                // Modified date
                if (panelTools.getColumnCodeName().contains(ProjectListItem.MODIFIED_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(ProjectListItem.MODIFIED_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(longDateFormat(project.getModifiedDate())), Element.ALIGN_LEFT));
                    } else {
                        mapColumn.put(ProjectListItem.MODIFIED_DATE, new ExcelData(longDateFormat(project.getModifiedDate()), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), project, edsCompany);
                excelDataList = new ArrayList<>();
                for (int j = 0; j < panelTools.getColumnCodeName().size(); j++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(j))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(j)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate project list excel report, exception: " + e);
        }
        return null;
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    private String formatIntToTime(int totalActualTime) {
        int minute = totalActualTime % 60;
        int hour = (totalActualTime - minute) / 60;
        return (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
    }
}
