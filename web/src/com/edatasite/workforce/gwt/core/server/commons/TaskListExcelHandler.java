package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(TaskListExcelHandler.class);
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskManager taskManager;
    @Autowired
    protected WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    private PropertManager propertManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "Task List";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        String sheetName = property != null ? property.getPlural() : pdfWfmMessageSource.localize("tasks");
        String shortDateFormat = "MM/dd/yyyy";
        EdsUser user = taskManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        long timeStarted = System.currentTimeMillis();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        TaskList taskList = taskService.getTaskList(filterParametrs);
        List<TaskListItem> tasks = new ArrayList<>(taskList.getList());
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(TaskListItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 40, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.customer), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.PROJECT_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.project), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.PROJECT_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.PROJECT_MANAGER_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.projectManager), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.BILLABLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.billable), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.PARENT_WORKSTREAM_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.workStream), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.ASSIGNED_TO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assignees), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.PRIORITY_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.priority), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.STATUS_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assigneeStatus), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.OVERALL_STATUS_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.overAllStatus), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.COMPLETE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.percentCompleted), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.ESTIMATED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.estimatedTime), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.HOUR_SPENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.ACTUAL_HOURS_SPENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.actualTimeSpent), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDateField), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.DUE_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.dueDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.DURATION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.duration), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.completedDateActual), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.ACTUAL_START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.actualStartDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.CREATED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.LAST_MODIFIED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.LAST_MODIFIED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.TASK_AMOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.taskAmount), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CONTACT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedContact), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_LEAD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedCrmAccount), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_CASE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedCase), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EVENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_PROJECT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToProject), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(RelationItem.TYPE_ISSUE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.TASK_RELATED_CLIENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            mapColumnHeader.put(TaskListItem.WAITING_HOURS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.waitingHours), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.REJECTED_HOURS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.rejectedHours), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TaskListItem.CREATION_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
            // Set excell header
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) : ServerUtils.shortDateFormat(user.getUserDate(new Date()), user) + " " + commonLocalizer.localize(PdfLocalizationName.asOF), workBook.getSheet(), 2));

            List<ExcelData> excellDatasList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excellDatasList.add(getExcelDataHeader(mapColumnHeader.get(panelTools.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excellDatasList.size()];
            excellDatasList.toArray(cellDatas);
            list.add(cellDatas);

            for (TaskListItem task : tasks) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(TaskListItem.NUMBER)) {
                    mapColumn.put(TaskListItem.NUMBER, new ExcelData(task.getNumber(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.NAME)) {
                    mapColumn.put(TaskListItem.NAME, new ExcelData(task.getName(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.DESCRIPTION)) {
                    mapColumn.put(TaskListItem.DESCRIPTION, new ExcelData(task.getDescription(), ExcelData.STRING, 40, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.CLIENT)) {
                    mapColumn.put(TaskListItem.CLIENT, new ExcelData(task.getClient(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_NAME)) {
                    mapColumn.put(TaskListItem.PROJECT_NAME, new ExcelData(task.getProjectName() != null ? task.getProjectName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_NUMBER)) {
                    mapColumn.put(TaskListItem.PROJECT_NUMBER, new ExcelData(task.getProjectNumber() != null ? task.getProjectNumber() : "", ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_MANAGER_NAME)) {
                    mapColumn.put(TaskListItem.PROJECT_MANAGER_NAME, new ExcelData(task.getProjectManagerName() != null ? task.getProjectManagerName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.TASK_AMOUNT)) {
                    mapColumn.put(TaskListItem.TASK_AMOUNT, new ExcelData(task.getTaskAmount() != null ? task.getTaskAmount() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.BILLABLE)) {
                    mapColumn.put(TaskListItem.BILLABLE, new ExcelData(task.isBillable() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.PARENT_WORKSTREAM_NAME)) {
                    mapColumn.put(TaskListItem.PARENT_WORKSTREAM_NAME, new ExcelData(task.getParentWorkstreamName() != null ? task.getParentWorkstreamName() : "", ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.STATUS_NAME)) {
                    mapColumn.put(TaskListItem.STATUS_NAME, new ExcelData(task.getStatusName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.OVERALL_STATUS_NAME)) {
                    mapColumn.put(TaskListItem.OVERALL_STATUS_NAME, new ExcelData(task.getOverallStatusName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.PRIORITY_NAME)) {
                    mapColumn.put(TaskListItem.PRIORITY_NAME, new ExcelData(task.getPriorityName(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.HOUR_SPENT)) {
                    mapColumn.put(TaskListItem.HOUR_SPENT, new ExcelData(task.getHoursSpent(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.ACTUAL_HOURS_SPENT)) {
                    mapColumn.put(TaskListItem.ACTUAL_HOURS_SPENT, new ExcelData(task.getActualHoursSpent(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.COMPLETE)) {
                    mapColumn.put(TaskListItem.COMPLETE, new ExcelData(formatToDouble(task.getComplete() != null && !"".equals(task.getComplete()) && !"null".equals(task.getComplete()) ? task.getComplete() : "0.0"), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.DURATION)) {
                    mapColumn.put(TaskListItem.DURATION, new ExcelData(String.valueOf(task.calculateDueDays()), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.START_DATE)) {
                    String startDate = "N/A";
                    if (task.isAllDay() != null && task.isAllDay()) {
                        startDate = task.getStartDate() != null ? ServerUtils.shortDateFormat(user.getUserDate(task.getStartDate()), user) : "N/A";
                    } else {
                        startDate = task.getStartDate() != null ? ServerUtils.longDateFormat(user.getUserDate(task.getStartDate()), user) : "N/A";
                    }
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(TaskListItem.START_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(startDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(TaskListItem.START_DATE, new ExcelData(startDate, ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.ACTUAL_START_DATE)) {
                    String actualStartDate = "N/A";
                    if (task.isAllDay() != null && task.isAllDay()) {
                        actualStartDate = task.getActualStartDate() != null ? ServerUtils.shortDateFormat(user.getUserDate(task.getActualStartDate()), user) : "N/A";
                    } else {
                        actualStartDate = task.getActualStartDate() != null ? ServerUtils.longDateFormat(user.getUserDate(task.getActualStartDate()), user) : "N/A";
                    }
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(TaskListItem.ACTUAL_START_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(actualStartDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(TaskListItem.ACTUAL_START_DATE, new ExcelData(actualStartDate, ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.DUE_DATE)) {
                    String dueDateS = "N/A";
                    if (task.isAllDay() != null && task.isAllDay()) {
                        dueDateS = task.getDueDate() != null ? ServerUtils.shortDateFormat(user.getUserDate(task.getDueDate()), user) : "N/A";
                    } else {
                        dueDateS = task.getDueDate() != null ? ServerUtils.longDateFormat(user.getUserDate(task.getDueDate()), user) : "N/A";
                    }
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(TaskListItem.DUE_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(dueDateS), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(TaskListItem.DUE_DATE, new ExcelData(dueDateS, ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.END_DATE)) {
                    mapColumn.put(TaskListItem.END_DATE, new ExcelData(task.getEndDate() != null ? task.getEndDate() : "", ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.ASSIGNED_TO)) {
                    List<EdsProjectEmployee> pes = taskManager.getTaskAssignees(task.getObjectID());
                    mapColumn.put(TaskListItem.ASSIGNED_TO, new ExcelData(getAssignments(new HashSet<>(pes)), ExcelData.STRING, 30, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.ESTIMATED)) {
                    String estimateTime = task.getEstimated() != null ? ServerUtils.timeSpentToString(task.getEstimated()) : "00:00";
                    mapColumn.put(TaskListItem.ESTIMATED, new ExcelData(estimateTime, ExcelData.STRING, 7, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.CREATED_BY)) {
                    mapColumn.put(TaskListItem.CREATED_BY, new ExcelData(task.getCreatedBy(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED)) {
                    String modifiedDate = ServerUtils.longDateFormat(taskManager.getUser().getUserDate(task.getLastModified()), taskManager.getUser());
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(TaskListItem.LAST_MODIFIED, new ExcelData(ServerUtils.convertToUzbDateFormat(modifiedDate), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(TaskListItem.LAST_MODIFIED, new ExcelData(modifiedDate, ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    }
                }

                if (panelTools.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED_BY)) {
                    mapColumn.put(TaskListItem.LAST_MODIFIED_BY, new ExcelData(task.getLastModifiedBy(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                    mapColumn.put(RelationItem.TYPE_CONTACT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_CONTACT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                    mapColumn.put(RelationItem.TYPE_LEAD, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_LEAD), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                    mapColumn.put(RelationItem.TYPE_CRM_ACCOUNT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    mapColumn.put(RelationItem.TYPE_CASE, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_CASE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                    mapColumn.put(RelationItem.TYPE_PROJECT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_PROJECT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                    mapColumn.put(RelationItem.TYPE_OPPORTUNITY, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                    mapColumn.put(RelationItem.TYPE_EVENT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_EVENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related issue
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                    mapColumn.put(RelationItem.TYPE_ISSUE, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_ISSUE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related employee
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                    mapColumn.put(RelationItem.TYPE_EMPLOYEE, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related department
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                    mapColumn.put(RelationItem.TYPE_DEPARTMENT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related client
                if (panelTools.getColumnCodeName().contains(TaskListItem.TASK_RELATED_CLIENT)) {
                    mapColumn.put(TaskListItem.TASK_RELATED_CLIENT, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_CLIENT), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //related supplier
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                    mapColumn.put(RelationItem.TYPE_SUPPLIER, new ExcelData(task.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.WAITING_HOURS)) {
                    mapColumn.put(TaskListItem.WAITING_HOURS, new ExcelData(task.getWaitingHours(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.REJECTED_HOURS)) {
                    mapColumn.put(TaskListItem.REJECTED_HOURS, new ExcelData(task.getRejectedHours(), ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TaskListItem.CREATION_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        mapColumn.put(TaskListItem.CREATION_DATE, new ExcelData(ServerUtils.convertToUzbDateFormat(longDateFormat(task.getCreationDate())), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        mapColumn.put(TaskListItem.CREATION_DATE, new ExcelData(longDateFormat(task.getCreationDate()), ExcelData.STRING, 12, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), task, edsCompany);
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
            System.out.println(excelReferenceMessageSource.localize("ProfilingExcelData", "Profiling excel Data generation, time spent:") + (System.currentTimeMillis() - timeStarted));
            //timeStarted  = System.currentTimeMillis();
            //WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            //System.out.println("Profiling excel workbook generation, time spent:" + (System.currentTimeMillis() - timeStarted));
            return workBook.getWorkBook(filename, 0, 0, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate task list excel report, exception: " + e);
        }
        return null;
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    private String formatToDouble(String text) {
        return getMoneyFormat(parseToDouble(text));
    }

    private String getAssignments(Set<EdsProjectEmployee> assigments) {
        StringBuilder buf = new StringBuilder();
        try {
            int i = 0;
            for (Object empTask : assigments.toArray()) {
                if (((EdsProjectEmployee) empTask).getEmployeeDepartment() != null) {
                    buf.append(((EdsProjectEmployee) empTask).getEmployeeDepartment().getEmployee().getFullName());
                    if (++i < assigments.toArray().length) {
                        buf.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

}
