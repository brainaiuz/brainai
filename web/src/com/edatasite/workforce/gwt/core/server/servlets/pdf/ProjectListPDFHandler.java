package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectListPDFHandler extends AbstractITextPostPdfHandler {
    private ProjectService projectService;

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
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
        ListResult<ProjectListItem> projList = projectService.getProjectList(filterParametrs);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(ProjectListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.MANAGER, new CellData(commonLocalizer.localize(PdfLocalizationName.manager), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.BACKUP_MANAGER, new CellData(commonLocalizer.localize(PdfLocalizationName.backupManager), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.INCOME, new CellData(commonLocalizer.localize(PdfLocalizationName.income), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.PROFIT, new CellData(commonLocalizer.localize(PdfLocalizationName.profit), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.COST, new CellData(commonLocalizer.localize(PdfLocalizationName.cost), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.PLANED_PROFIT, new CellData(commonLocalizer.localize(PdfLocalizationName.planedProfit), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.PLANED_COST, new CellData(commonLocalizer.localize(PdfLocalizationName.planedCost), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.PLANED_INCOME, new CellData(commonLocalizer.localize(PdfLocalizationName.planedIncome), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.ACTUAL_TIME_SPENT, new CellData(commonLocalizer.localize(PdfLocalizationName.actualTimeSpent), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.HOURS_SPENT, new CellData(commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.NUMBER_OF_TASKS, new CellData(commonLocalizer.localize(PdfLocalizationName.noOfTask), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.HEAD_COUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.headCount), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.PERCENT_COMPLETED, new CellData(commonLocalizer.localize(PdfLocalizationName.percentCompleted), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDateField), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.ESTIMATED_TIME, new CellData(commonLocalizer.localize(PdfLocalizationName.estimatedTime), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CONTACT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_LEAD, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToCRMAccount), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CASE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedCase), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_OPPORTUNITY, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EVENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_TASK, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedTask), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_ISSUE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.PROJECT_RELATION_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.INVOICES, new CellData(commonLocalizer.localize(PdfLocalizationName.invoice), Element.ALIGN_RIGHT));
        columnHeaderMap.put(ProjectListItem.CREATED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.CONTRACT, new CellData(commonLocalizer.localize(PdfLocalizationName.contract), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.WAITING_HOURS, new CellData(commonLocalizer.localize(PdfLocalizationName.waitingForApproval), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.REJECTED_HOURS, new CellData(pmLocalizer.localize(PdfLocalizationName.rejectedHours), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.BILLABLE, new CellData(commonLocalizer.localize(PdfLocalizationName.billable), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.MODIFIED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(ProjectListItem.MODIFIED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (CollectionUtils.isNotEmpty(projList.getList())) {
            for (ProjectListItem project : projList.getList()) {
                String projectNum = getResultOrLongDash(project.getNumber());
                String tasksCount = project.getTaskCount() != null ? project.getTaskCount().toString() : "0";
                String headsCount = project.getHeadCount() != null ? project.getHeadCount().toString() : "0";
                String percentCompleted = project.getComplete() != null ? project.getComplete() : "0.00";

                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NUMBER)) {
                    columnMap.put(ProjectListItem.NUMBER, new CellData(projectNum, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NAME)) {
                    columnMap.put(ProjectListItem.NAME, new CellData(getResultOrLongDash(project.getName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.DESCRIPTION)) {
                    columnMap.put(ProjectListItem.DESCRIPTION, new CellData(getResultOrLongDash(project.getDescription()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.MANAGER)) {
                    columnMap.put(ProjectListItem.MANAGER, new CellData(getResultOrLongDash(project.getManager()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.BACKUP_MANAGER)) {
                    columnMap.put(ProjectListItem.BACKUP_MANAGER, new CellData(getResultOrLongDash(project.getBackupManager()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CLIENT)) {
                    columnMap.put(ProjectListItem.CLIENT, new CellData(getResultOrLongDash(project.getClient()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.INCOME)) {
                    columnMap.put(ProjectListItem.INCOME, new CellData(getMoneyFormat(project.getIncome()), Element.ALIGN_RIGHT));
                }
                //profit
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROFIT)) {
                    columnMap.put(ProjectListItem.PROFIT, new CellData(getMoneyFormat(project.getProfit()), Element.ALIGN_RIGHT));
                }
                //cost
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_COST)) {
                    columnMap.put(ProjectListItem.COST, new CellData(getMoneyFormat(project.getCost()), Element.ALIGN_RIGHT));
                }
                //planned_profit
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_PROFIT)) {
                    columnMap.put(ProjectListItem.PLANED_PROFIT, new CellData(getMoneyFormat(project.getPlanedProfit()), Element.ALIGN_RIGHT));
                }
                //planned_cost
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_COST)) {
                    columnMap.put(ProjectListItem.PLANED_COST, new CellData(getMoneyFormat(project.getPlanedCost()), Element.ALIGN_RIGHT));
                }
                //planned_income
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PLANED_INCOME)) {
                    columnMap.put(ProjectListItem.PLANED_INCOME, new CellData(getMoneyFormat(project.getPlanedIncome()), Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.ACTUAL_TIME_SPENT)) {
                    columnMap.put(ProjectListItem.ACTUAL_TIME_SPENT, new CellData(getResultOrLongDash(project.getActualHoursSpent()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.HOURS_SPENT)) {
                    columnMap.put(ProjectListItem.HOURS_SPENT, new CellData(getResultOrLongDash(project.getHoursSpent()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.NUMBER_OF_TASKS)) {
                    columnMap.put(ProjectListItem.NUMBER_OF_TASKS, new CellData(tasksCount, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.HEAD_COUNT)) {
                    columnMap.put(ProjectListItem.HEAD_COUNT, new CellData(headsCount, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.STATUS)) {
                    columnMap.put(ProjectListItem.STATUS, new CellData(getResultOrLongDash(project.getStatus()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PERCENT_COMPLETED)) {
                    columnMap.put(ProjectListItem.PERCENT_COMPLETED, new CellData(percentCompleted, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.START_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(ProjectListItem.START_DATE, new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(project.getStartDate())), Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(ProjectListItem.START_DATE, new CellData(dateFormat(project.getStartDate()), Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.END_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(ProjectListItem.END_DATE, new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(project.getEndDate())), Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(ProjectListItem.END_DATE, new CellData(dateFormat(project.getEndDate()), Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.ESTIMATED_TIME)) {
                    columnMap.put(ProjectListItem.ESTIMATED_TIME, project.getEstimatedTime() != null ? new CellData(formatIntToTime(project.getEstimatedTime()), Element.ALIGN_LEFT) : new CellData("00:00"));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.LOCATION)) {
                    columnMap.put(ProjectListItem.LOCATION, new CellData(getResultOrLongDash(project.getProjectLocation()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                    columnMap.put(RelationItem.TYPE_CONTACT, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_CONTACT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                    columnMap.put(RelationItem.TYPE_LEAD, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_LEAD), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                    columnMap.put(RelationItem.TYPE_CRM_ACCOUNT, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                    columnMap.put(RelationItem.TYPE_CASE, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_CASE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                    columnMap.put(RelationItem.TYPE_OPPORTUNITY, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                    columnMap.put(RelationItem.TYPE_EVENT, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_EVENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                    columnMap.put(RelationItem.TYPE_TASK, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_TASK), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                //related issue
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                    columnMap.put(RelationItem.TYPE_ISSUE, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_ISSUE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                //related employee
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                    columnMap.put(RelationItem.TYPE_EMPLOYEE, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                //related department
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                    columnMap.put(RelationItem.TYPE_DEPARTMENT, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                //related client
                if (panelTools.getColumnCodeName().contains(ProjectListItem.PROJECT_RELATION_CLIENT)) {
                    columnMap.put(ProjectListItem.PROJECT_RELATION_CLIENT, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_CLIENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                //related supplier
                if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                    columnMap.put(RelationItem.TYPE_SUPPLIER, project.getRelationValueMap() != null ? new CellData(project.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CREATED_BY)) {
                    columnMap.put(ProjectListItem.CREATED_BY, new CellData(getResultOrLongDash(project.getCreatedBy()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CONTRACT)) {
                    columnMap.put(ProjectListItem.CONTRACT, new CellData(getResultOrLongDash(project.getContractName()), Element.ALIGN_LEFT));
                }
                //related invoice
                if (panelTools.getColumnCodeName().contains(ProjectListItem.INVOICES)) {
                    columnMap.put(ProjectListItem.INVOICES, new CellData(getResultOrLongDash(project.getInvoiceNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.WAITING_HOURS)) {
                    columnMap.put(ProjectListItem.WAITING_HOURS, new CellData(getResultOrLongDash(project.getWaitingHours()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.REJECTED_HOURS)) {
                    columnMap.put(ProjectListItem.REJECTED_HOURS, new CellData(getResultOrLongDash(project.getRejectedHours()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(ProjectListItem.BILLABLE)) {
                    columnMap.put(ProjectListItem.BILLABLE, new CellData(project.getBillable() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT));
                }
                // Creation date
                if (panelTools.getColumnCodeName().contains(ProjectListItem.CREATED_DATE)) {
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        columnMap.put(ProjectListItem.CREATED_DATE, new CellData(ServerUtils.convertToUzbDateFormat(longDateFormat(project.getCreatedDate())), Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(ProjectListItem.CREATED_DATE, new CellData(longDateFormat(project.getCreatedDate()), Element.ALIGN_LEFT));
                    }
                }
                // Modified_by
                if (panelTools.getColumnCodeName().contains(ProjectListItem.MODIFIED_BY)) {
                    columnMap.put(ProjectListItem.MODIFIED_BY, new CellData(getResultOrLongDash(project.getModifiedBy()), Element.ALIGN_LEFT));
                }
                // Modified_date
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(ProjectListItem.MODIFIED_DATE, new CellData(ServerUtils.convertToUzbDateFormat(longDateFormat(project.getCreatedDate())), Element.ALIGN_LEFT));
                } else {
                    columnMap.put(ProjectListItem.MODIFIED_DATE, new CellData(longDateFormat(project.getModifiedDate()), Element.ALIGN_LEFT));
                }


                CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), project, company);
                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnCode -> columnMap.containsKey(columnCode))
                        .map(columnCode -> columnMap.get(columnCode))
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }
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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("projects");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.projectListFileName) + "_" + (dateFormat(new Date())));
    }

    private String formatIntToTime(int totalActualTime) {
        int minute = totalActualTime % 60;
        int hour = (totalActualTime - minute) / 60;
        return (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
    }
}
