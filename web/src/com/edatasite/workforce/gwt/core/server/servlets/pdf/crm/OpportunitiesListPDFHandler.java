package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Ilhombek
 * Date: 13.08.2009
 * Time: 16:59:34
 */

public class OpportunitiesListPDFHandler extends AbstractITextPostPdfHandler {

    private CRMService crmService;
    private static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

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
        filterParametrs.setFromExcelPDF(true);

        ListResult<OpportunityListItem> opportunityList = crmService.getOpportunityList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(OpportunityListItem.NUMBER, new CellData(crmLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.ASSIGNEE_NAME, new CellData(crmLocalizer.localize(PdfLocalizationName.assignee), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(OpportunityListItem.STAGE, new CellData(crmLocalizer.localize(PdfLocalizationName.stage), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.CLOSING_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.closeDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.ACCOUNT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.company), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.ISCONVERTEDTOPROJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.isConvertedToProject), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CONTACT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_LEAD, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedLead), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedCrmAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CASE, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedCase), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_TASK, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedTask), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_EVENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEvent), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_PROJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedProject), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_ISSUE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.contactName), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE, new CellData(commonLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, new CellData(crmLocalizer.localize(PdfLocalizationName.source), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.COUNTRY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.CREATOR_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.BACKUP_ASSIGNEE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.backupAssignee), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(OpportunityListItem.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));


        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (OpportunityListItem item : opportunityList.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.NUMBER)) {
                mapColumns.put(OpportunityListItem.NUMBER, item.getNumberData() != null ? new CellData(item.getNumberData().getNumberString(), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.ASSIGNEE_NAME)) {
                mapColumns.put(OpportunityListItem.ASSIGNEE_NAME, new CellData(getResultOrLongDash(item.getAssignee()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_NAME)) {
                mapColumns.put(OpportunityListItem.OPPORTUNITY_NAME, new CellData(getResultOrLongDash(item.getOpportunityName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.AMOUNT)) {
                String amountStr = "0.00";
                if (item.getAmount() != null) {
                    amountStr = numberFormat.format(item.getAmount());
                    amountStr = amountStr.replaceAll("[$]", "");
                }
                mapColumns.put(OpportunityListItem.AMOUNT, new CellData(amountStr + " " + ((!"".equals(item.getCurrency()) && item.getCurrency() != null) ? "(" + item.getCurrency() + ")" : ""), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.STAGE)) {
                mapColumns.put(OpportunityListItem.STAGE, new CellData(getResultOrLongDash(item.getStageName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.CLOSING_DATE)) {
                mapColumns.put(OpportunityListItem.CLOSING_DATE, item.getClosingDate() != null ? new CellData((ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getClosingDate())) : dateFormat(item.getClosingDate())), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.ACCOUNT_NAME)) {
                mapColumns.put(OpportunityListItem.ACCOUNT_NAME, new CellData(getResultOrLongDash(item.getAccount())));
            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.UPDATED_DATE)) {

                mapColumns.put(OpportunityListItem.UPDATED_DATE, new CellData(getResultOrLongDash(longDateFormat(item.getUpdatedDate())), Element.ALIGN_LEFT));

            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.CREATED_DATE)) {

                mapColumns.put(OpportunityListItem.CREATED_DATE, new CellData(getResultOrLongDash(longDateFormat(item.getCreatedDate())), Element.ALIGN_LEFT));

            }
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.ISCONVERTEDTOPROJECT)) {
                mapColumns.put(OpportunityListItem.ISCONVERTEDTOPROJECT, item.isConvertedToProject() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                mapColumns.put(RelationItem.TYPE_CONTACT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CONTACT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                mapColumns.put(RelationItem.TYPE_LEAD, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_LEAD), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                mapColumns.put(RelationItem.TYPE_CRM_ACCOUNT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                mapColumns.put(RelationItem.TYPE_CASE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CASE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                mapColumns.put(RelationItem.TYPE_TASK, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_TASK), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EVENT)) {
                mapColumns.put(RelationItem.TYPE_EVENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EVENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                mapColumns.put(RelationItem.TYPE_PROJECT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_PROJECT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related issue
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                mapColumns.put(RelationItem.TYPE_ISSUE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_ISSUE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related employee
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                mapColumns.put(RelationItem.TYPE_EMPLOYEE, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related department
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                mapColumns.put(RelationItem.TYPE_DEPARTMENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related client
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
                mapColumns.put(RelationItem.TYPE_CLIENT, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_CLIENT), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //related supplier
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                mapColumns.put(RelationItem.TYPE_SUPPLIER, item.getRelationValueMap() != null ? new CellData(item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //Contact Name
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_NAME)) {
                mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_NAME, new CellData(getResultOrLongDash(item.getContact()), Element.ALIGN_LEFT));
            }
            //Contact Phone
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE)) {
                mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE, item.getContactPrimaryPhone() != null ? new CellData(Utils.formatPhoneNumber(item.getContactPrimaryPhone(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            //Contact Email
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL)) {
                mapColumns.put(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL, new CellData(getResultOrLongDash(item.getContactPrimaryEmail()), Element.ALIGN_LEFT));
            }

            //Country
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.COUNTRY_NAME)) {
                mapColumns.put(OpportunityListItem.COUNTRY_NAME, new CellData(getResultOrLongDash(item.getCountryName()), Element.ALIGN_LEFT));

            }
            //Created by
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.CREATOR_NAME)) {
                mapColumns.put(OpportunityListItem.CREATOR_NAME, new CellData(getResultOrLongDash(item.getCreatorName()), Element.ALIGN_LEFT));
            }

            //Backup Assignee
            if (panelTools.getColumnCodeName().contains(OpportunityListItem.BACKUP_ASSIGNEE_NAME)) {
                mapColumns.put(OpportunityListItem.BACKUP_ASSIGNEE_NAME, new CellData(getResultOrLongDash(item.getBackupAssignee()), Element.ALIGN_LEFT));
            }


            if (panelTools.getColumnCodeName().contains(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE)) {
                mapColumns.put(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, new CellData(getResultOrLongDash(item.getLeadSource()), Element.ALIGN_LEFT));
            }


            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    protected Object getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> entries = filterMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_OpportunityList_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("opportunities");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }
}
