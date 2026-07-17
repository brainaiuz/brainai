package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskListPDFHandler extends AbstractITextPostPdfHandler {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        TaskList taskList = taskService.getTaskList(filterParametrs);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(TaskListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.PROJECT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.projectName), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.PROJECT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.PROJECT_MANAGER_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.projectManager), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.BILLABLE, new CellData(commonLocalizer.localize(PdfLocalizationName.billable), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.ASSIGNED_TO, new CellData(commonLocalizer.localize(PdfLocalizationName.assignees), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.STATUS_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.assigneeStatus), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.PRIORITY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.priority), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.ESTIMATED, new CellData(commonLocalizer.localize(PdfLocalizationName.estimatedTime), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.HOUR_SPENT, new CellData(commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.ACTUAL_HOURS_SPENT, new CellData(commonLocalizer.localize(PdfLocalizationName.actualTimeSpent), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.COMPLETE, new CellData(commonLocalizer.localize(PdfLocalizationName.percent), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.DUE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.DURATION, new CellData(commonLocalizer.localize(PdfLocalizationName.duration), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.TASK_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.taskAmount), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.REJECTED_HOURS, new CellData(commonLocalizer.localize(PdfLocalizationName.rejectedHours), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.ACTUAL_START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.actualStartDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.WAITING_HOURS, new CellData(commonLocalizer.localize(PdfLocalizationName.waitingHours), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.completedDateActual), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.CREATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.LAST_MODIFIED, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.LAST_MODIFIED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.PARENT_WORKSTREAM_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.workStream), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.OVERALL_STATUS_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.overAllStatus), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CONTACT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_LEAD, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedCrmAccount), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CASE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedCase), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_OPPORTUNITY, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EVENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), Element.ALIGN_LEFT));


        columnHeaderMap.put(RelationItem.TYPE_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToProject), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_ISSUE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.TASK_RELATED_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.WAITING_HOURS, new CellData(commonLocalizer.localize(PdfLocalizationName.waitingHours), Element.ALIGN_LEFT));
        columnHeaderMap.put(TaskListItem.REJECTED_HOURS, new CellData(commonLocalizer.localize(PdfLocalizationName.rejectedHours), Element.ALIGN_LEFT));

        columnHeaderMap.put(TaskListItem.CREATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (TaskListItem item : taskList.getList()) {
            Map<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(TaskListItem.NUMBER)) {
                columnMap.put(TaskListItem.NUMBER, new CellData(getResultOrLongDash(item.getNumber()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.NAME)) {
                columnMap.put(TaskListItem.NAME, new CellData(getResultOrLongDash(item.getName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.DESCRIPTION)) {
                columnMap.put(TaskListItem.DESCRIPTION, new CellData(getResultOrLongDash(item.getDescription()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.CLIENT)) {
                columnMap.put(TaskListItem.CLIENT, new CellData(getResultOrLongDash(item.getClient()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_NAME)) {
                columnMap.put(TaskListItem.PROJECT_NAME, new CellData(getResultOrLongDash(item.getProjectName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_NUMBER)) {
                columnMap.put(TaskListItem.PROJECT_NUMBER, new CellData(getResultOrLongDash(item.getProjectNumber()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.PROJECT_MANAGER_NAME)) {
                columnMap.put(TaskListItem.PROJECT_MANAGER_NAME, new CellData(getResultOrLongDash(item.getProjectManagerName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.BILLABLE)) {
                columnMap.put(TaskListItem.BILLABLE, (item.isBillable() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT)));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.ASSIGNED_TO)) {
                columnMap.put(TaskListItem.ASSIGNED_TO, new CellData(getResultOrLongDash(item.getAssignedTo().replace(",", "\n")), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.STATUS_NAME)) {
                columnMap.put(TaskListItem.STATUS_NAME, new CellData(getResultOrLongDash(item.getStatusName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.PRIORITY_NAME)) {
                columnMap.put(TaskListItem.PRIORITY_NAME, new CellData(getResultOrLongDash(item.getPriorityName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.COMPLETE)) {
                columnMap.put(TaskListItem.COMPLETE, new CellData(getResultOrLongDash(item.getComplete()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.END_DATE)) {
                columnMap.put(TaskListItem.END_DATE, new CellData(getResultOrLongDash(String.valueOf(item.getEndDate())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.HOUR_SPENT)) {
                columnMap.put(TaskListItem.HOUR_SPENT, new CellData(getResultOrLongDash(item.getHoursSpent()), Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(TaskListItem.WAITING_HOURS)) {
                columnMap.put(TaskListItem.WAITING_HOURS, new CellData(getResultOrLongDash(item.getWaitingHours()), Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(TaskListItem.REJECTED_HOURS)) {
                columnMap.put(TaskListItem.REJECTED_HOURS, new CellData(getResultOrLongDash(item.getRejectedHours()), Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(TaskListItem.ACTUAL_HOURS_SPENT)) {
                columnMap.put(TaskListItem.ACTUAL_HOURS_SPENT, new CellData(getResultOrLongDash(item.getActualHoursSpent()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.OVERALL_STATUS_NAME)) {
                columnMap.put(TaskListItem.OVERALL_STATUS_NAME, new CellData(getResultOrLongDash(item.getOverallStatusName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.REJECTED_HOURS)) {
                columnMap.put(TaskListItem.REJECTED_HOURS, new CellData(getResultOrLongDash(item.getRejectedHours()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.TASK_AMOUNT)) {
                columnMap.put(TaskListItem.TASK_AMOUNT, new CellData(getResultOrLongDash(String.valueOf(item.getTaskAmount())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.WAITING_HOURS)) {
                columnMap.put(TaskListItem.WAITING_HOURS, new CellData(getResultOrLongDash(item.getWaitingHours()), Element.ALIGN_LEFT));
            }


            if (panelTools.getColumnCodeName().contains(TaskListItem.ACTUAL_START_DATE)) {
                columnMap.put(TaskListItem.ACTUAL_START_DATE, new CellData(getResultOrLongDash(String.valueOf(item.getActualStartDate())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.START_DATE)) {
                String startDate = "—";
                if (item.isAllDay() != null && item.isAllDay()) {
                    startDate = item.getStartDate() != null ? dateFormat(item.getStartDate()) : "—";
                } else {
                    startDate = item.getStartDate() != null ? longDateFormat(item.getStartDate()) : "—";
                }
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(TaskListItem.START_DATE, new CellData(getResultOrLongDash(ServerUtils.convertToUzbDateFormat(startDate)), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(TaskListItem.START_DATE, new CellData(startDate, Element.ALIGN_LEFT));
                }
            }

            if (panelTools.getColumnCodeName().contains(TaskListItem.DURATION)) {
                String dueDaysValue = item.calculateDueDays();
                columnMap.put(TaskListItem.DURATION, new CellData(getResultOrLongDash(dueDaysValue), Element.ALIGN_LEFT));
            }

            if (panelTools.getColumnCodeName().contains(TaskListItem.DUE_DATE)) {
                String dueDateS = "—";
                if (item.isAllDay() != null && item.isAllDay()) {
                    dueDateS = item.getDueDate() != null ? dateFormat(item.getDueDate()) : "—";
                } else {
                    dueDateS = item.getDueDate() != null ? longDateFormat(item.getDueDate()) : "—";
                }
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(TaskListItem.DUE_DATE, new CellData(getResultOrLongDash(ServerUtils.convertToUzbDateFormat(dueDateS)), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(TaskListItem.DUE_DATE, new CellData(dueDateS, Element.ALIGN_LEFT));
                }
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.ESTIMATED)) {
                columnMap.put(TaskListItem.ESTIMATED, item.getEstimated() != null ? new CellData(ServerUtils.timeSpentToString(item.getEstimated()), Element.ALIGN_LEFT) : new CellData("00:00", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.CREATED_BY)) {
                columnMap.put(TaskListItem.CREATED_BY, new CellData(getResultOrLongDash(item.getCreatedBy()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(TaskListItem.LAST_MODIFIED, item.getLastModified() != null ? new CellData(ServerUtils.convertToUzbDateFormat(longDateFormat(item.getLastModified())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                } else {
                    columnMap.put(TaskListItem.LAST_MODIFIED, item.getLastModified() != null ? new CellData(longDateFormat(item.getLastModified()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));

                }
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED_BY)) {
                columnMap.put(TaskListItem.LAST_MODIFIED_BY, new CellData(getResultOrLongDash(item.getLastModifiedBy()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(TaskListItem.PARENT_WORKSTREAM_NAME)) {
                columnMap.put(TaskListItem.PARENT_WORKSTREAM_NAME, new CellData(getResultOrLongDash(item.getParentWorkstreamName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                columnMap.put(RelationItem.TYPE_CONTACT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                columnMap.put(RelationItem.TYPE_LEAD, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                columnMap.put(RelationItem.TYPE_CRM_ACCOUNT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                columnMap.put(RelationItem.TYPE_CASE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                columnMap.put(RelationItem.TYPE_OPPORTUNITY, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                columnMap.put(RelationItem.TYPE_EVENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related issue
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                columnMap.put(RelationItem.TYPE_ISSUE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_ISSUE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related employee
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                columnMap.put(RelationItem.TYPE_EMPLOYEE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related department
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                columnMap.put(RelationItem.TYPE_DEPARTMENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related client
            if (panelTools.getColumnCodeName().contains(TaskListItem.TASK_RELATED_CLIENT)) {
                columnMap.put(TaskListItem.TASK_RELATED_CLIENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related supplier
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                columnMap.put(RelationItem.TYPE_SUPPLIER, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }

            //related project
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                columnMap.put(RelationItem.TYPE_PROJECT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            // Created date

            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                columnMap.put(TaskListItem.CREATION_DATE, new CellData(ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreationDate())), Element.ALIGN_LEFT));
            } else {
                columnMap.put(TaskListItem.CREATION_DATE, new CellData(longDateFormat(item.getCreationDate()), Element.ALIGN_LEFT));
            }


            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), item, company);
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> columnMap.containsKey(columnCode))
                    .map(columnCode -> columnMap.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("tasks");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.taskListFileName) + "_" + dateFormat(new Date()));
    }
}
