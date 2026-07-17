package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
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
 * User: Ilhombek
 * Date: 13.08.2009
 * Time: 19:16:08
 */
public class CaseListPDFHandler extends AbstractITextPostPdfHandler {

    private CRMService crmService;

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
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(true);

        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        long startedAt = System.currentTimeMillis();
        System.out.println("Get case list for pdf started at:===========================" + new Date() + "===========================");
        CaseList caseList = crmService.getCases(filterParametrs);
        System.out.println("It took to get case list:===========================" + (System.currentTimeMillis() - startedAt) + "===========================");
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CaseItem> caseItems = caseList.getList();

        Map<String, CellData> columnHeaderMap = getColumnHeaderMap();

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        long startedToDraw = System.currentTimeMillis();
        System.out.println("It's started to draw case list pdf at:===========================" + new Date() + "===========================");
        for (CaseItem item : caseItems) {
            Map<String, CellData> columnMap = getColumnMapValues(panelTools, item);

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), item, company);
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> columnMap.containsKey(columnCode))
                    .map(columnCode -> columnMap.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }
        System.out.println("It took to draw case list:===========================" + (System.currentTimeMillis() - startedToDraw) + "===========================");

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    private Map<String, CellData> getColumnHeaderMap() {
        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(CaseItem.CASE_ID, new CellData(crmLocalizer.localize(PdfLocalizationName.caseID), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.SUBJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.subject), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.PRIORITY, new CellData(commonLocalizer.localize(PdfLocalizationName.priority), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.REPORTED_BY, new CellData(crmLocalizer.localize(PdfLocalizationName.reportedBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.CREATED_DATE, new CellData(crmLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.LAST_UPDATED_DATE, new CellData(crmLocalizer.localize(PdfLocalizationName.lastUpdated), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.ASSIGNED_TO, new CellData(commonLocalizer.localize(PdfLocalizationName.assignedTo), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.CASE_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.CASE_REASON, new CellData(commonLocalizer.localize(PdfLocalizationName.caseReason), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.BILLABLE, new CellData(commonLocalizer.localize(PdfLocalizationName.billable), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.RESOLVER, new CellData(commonLocalizer.localize(PdfLocalizationName.resolver), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.ORIGIN, new CellData(commonLocalizer.localize(PdfLocalizationName.origin), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CONTACT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_LEAD, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedLead), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedCRMAccount), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_OPPORTUNITY, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedOpportunity), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_TASK, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedTask), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EVENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_PROJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedProject), Element.ALIGN_LEFT));

        columnHeaderMap.put(RelationItem.TYPE_ISSUE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        columnHeaderMap.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        columnHeaderMap.put(CaseItem.REPORTED_BY_COMPANY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.reportedBy), Element.ALIGN_LEFT));

        return columnHeaderMap;
    }

    private Map<String, CellData> getColumnMapValues(ListPanelToolRpc panelTools, CaseItem item) {
        Map<String, CellData> columnMap = new HashMap<>();
        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_ID)) {
            columnMap.put(CaseItem.CASE_ID, new CellData(getResultOrLongDash(item.getCaseNumber()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.SUBJECT)) {
            columnMap.put(CaseItem.SUBJECT, new CellData(getResultOrLongDash(item.getSubject()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.PRIORITY)) {
            columnMap.put(CaseItem.PRIORITY, new CellData(getResultOrLongDash(item.getPriority()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.REPORTED_BY)) {
            columnMap.put(CaseItem.REPORTED_BY, new CellData(getResultOrLongDash(item.getReportedBy()), Element.ALIGN_LEFT));
        }

        if (panelTools.getColumnCodeName().contains(CaseItem.REPORTED_BY_COMPANY_NAME)) {
            columnMap.put(CaseItem.REPORTED_BY_COMPANY_NAME, new CellData(getResultOrLongDash(item.getReportedByCompanyName()), Element.ALIGN_LEFT));
        }

        if (panelTools.getColumnCodeName().contains(CaseItem.CREATED_DATE)) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                columnMap.put(CaseItem.CREATED_DATE, new CellData(getResultOrLongDash(ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate()))), Element.ALIGN_LEFT));
            } else {

                columnMap.put(CaseItem.CREATED_DATE, new CellData(getResultOrLongDash(longDateFormat(item.getCreatedDate())), Element.ALIGN_LEFT));
            }
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.LAST_UPDATED_DATE)) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                columnMap.put(CaseItem.LAST_UPDATED_DATE, new CellData(ServerUtils.convertToUzbDateFormat(getResultOrLongDash(longDateFormat(item.getLastUpdatedDate()))), Element.ALIGN_LEFT));
            } else {

                columnMap.put(CaseItem.LAST_UPDATED_DATE, new CellData(getResultOrLongDash(longDateFormat(item.getLastUpdatedDate())), Element.ALIGN_LEFT));
            }
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.ASSIGNED_TO)) {
            columnMap.put(CaseItem.ASSIGNED_TO, new CellData(getResultOrLongDash(item.getCaseAssigneeName() == null ? item.getDepartment() : item.getCaseAssigneeName()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.STATUS)) {
            columnMap.put(CaseItem.STATUS, new CellData(item.getStatus().getId() != null ? item.getStatus().getName() : "—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.RESOLVER)) {
            columnMap.put(CaseItem.RESOLVER, new CellData(getResultOrLongDash(item.getResolverName()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_TYPE)) {
            columnMap.put(CaseItem.CASE_TYPE, new CellData(getResultOrLongDash(item.getType()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.CASE_REASON)) {
            columnMap.put(CaseItem.CASE_REASON, new CellData(getResultOrLongDash(item.getCaseReason()), Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(CaseItem.ORIGIN)) {
            columnMap.put(CaseItem.ORIGIN, new CellData(item.getCaseOrigin(), Element.ALIGN_LEFT));
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
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
            columnMap.put(RelationItem.TYPE_OPPORTUNITY, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
            columnMap.put(RelationItem.TYPE_TASK, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
            columnMap.put(RelationItem.TYPE_EVENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
            columnMap.put(RelationItem.TYPE_PROJECT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
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
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
            columnMap.put(RelationItem.TYPE_CLIENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }
        //related supplier
        if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
            columnMap.put(RelationItem.TYPE_SUPPLIER, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
        }

        return columnMap;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("cases");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_CaseList_" + dateFormat(new Date()));
    }

    public CRMService getCrmService() {
        return crmService;
    }

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }
}
