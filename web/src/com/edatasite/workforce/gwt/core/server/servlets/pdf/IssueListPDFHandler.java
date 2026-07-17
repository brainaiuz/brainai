package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
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

/**
 * User: Rinat
 * Date: 07.09.2011
 * Time: 12:32:18
 */

public class IssueListPDFHandler extends AbstractITextPostPdfHandler {

    private IssueService issueService;

    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_IssueList_" + dateFormat(new Date()));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<IssueListItem> issueItemListResult = issueService.getIssuesList(filterParametrs);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(IssueListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        columnHeaderMap.put("relatedTo", new CellData(commonLocalizer.localize(PdfLocalizationName.relatedTo), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.RESOLVER, new CellData(commonLocalizer.localize(PdfLocalizationName.resolver), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.PRIORITY, new CellData(commonLocalizer.localize(PdfLocalizationName.priority), Element.ALIGN_LEFT));

        columnHeaderMap.put(RelationItem.TYPE_CONTACT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_LEAD, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToLead), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToCRMAccount), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CASE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedCase), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_OPPORTUNITY, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToOpportunity), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EVENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_TASK, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedTask), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToProject), Element.ALIGN_LEFT));

        columnHeaderMap.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        columnHeaderMap.put(IssueListItem.TIMESHEET, new CellData(commonLocalizer.localize(PdfLocalizationName.timesheet), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (IssueListItem item : issueItemListResult.getList()) {
            Map<String, CellData> columnMap = new HashMap<>();

            if (panelTools.getColumnCodeName().contains(IssueListItem.NUMBER)) {
                columnMap.put(IssueListItem.NUMBER, new CellData(getResultOrLongDash(item.getNumber())));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.NAME)) {
                columnMap.put(IssueListItem.NAME, new CellData(getResultOrLongDash(item.getName())));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.DESCRIPTION)) {
                columnMap.put(IssueListItem.DESCRIPTION, new CellData(getResultOrLongDash(item.getDescription())));
            }
            if (panelTools.getColumnCodeName().contains("relatedTo")) {
                columnMap.put("relatedTo", new CellData(getResultOrLongDash(item.getRelatedTo())));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.PERIOD)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    columnMap.put(IssueListItem.PERIOD, item.getStartDate() != null && item.getEndDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate())) + " - " + ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())), Element.ALIGN_LEFT) : new CellData("—"));
                } else {
                    columnMap.put(IssueListItem.PERIOD, item.getStartDate() != null && item.getEndDate() != null ? new CellData(dateFormat(item.getStartDate()) + " - " + dateFormat(item.getEndDate()), Element.ALIGN_LEFT) : new CellData("—"));

                }
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.STATUS)) {
                columnMap.put(IssueListItem.STATUS, new CellData(getResultOrLongDash(item.getStatus())));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.RESOLVER)) {
                columnMap.put(IssueListItem.RESOLVER, new CellData(getResultOrLongDash(item.getResolver())));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.TIMESHEET)) {
                columnMap.put(IssueListItem.TIMESHEET, item.isTimeSheetEnabled() ? new CellData(commonLocalizer.localize(PdfLocalizationName.enabled), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.disabled), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(IssueListItem.PRIORITY)) {
                columnMap.put(IssueListItem.PRIORITY, new CellData(getResultOrLongDash(item.getPriority())));
            }

            //related contact
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                columnMap.put(RelationItem.TYPE_CONTACT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related lead
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                columnMap.put(RelationItem.TYPE_LEAD, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related crm account
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                columnMap.put(RelationItem.TYPE_CRM_ACCOUNT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related case
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                columnMap.put(RelationItem.TYPE_CASE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related opportunity
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                columnMap.put(RelationItem.TYPE_OPPORTUNITY, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related event
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                columnMap.put(RelationItem.TYPE_EVENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related task
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                columnMap.put(RelationItem.TYPE_TASK, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related project
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                columnMap.put(RelationItem.TYPE_PROJECT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
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
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
                columnMap.put(RelationItem.TYPE_CLIENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related supplier
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                columnMap.put(RelationItem.TYPE_SUPPLIER, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("issues");
    }
}
