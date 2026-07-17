package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Rinat
 * Date: 19.08.11
 * Time: 15:44
 */

public class EventListPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    private CRMService crmService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_EventsList_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("activityOnly");
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

        ListResult<EventItem> eventItemListResult = crmService.getEventList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(EventItem.SUBJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.subject), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDateField), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.VENUE, new CellData(commonLocalizer.localize(PdfLocalizationName.venue), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.DESCRIPTION, new CellData(crmLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CALL_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.callType), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.EVENT_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.ASSIGNEE, new CellData(commonLocalizer.localize(PdfLocalizationName.assignees), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CONTACT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_LEAD, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedLead), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CRM_ACCOUNT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedCrmAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CASE, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedCase), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_OPPORTUNITY, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedOpportunity), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_PROJECT, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedProject), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_TASK, new CellData(crmLocalizer.localize(PdfLocalizationName.relatedTask), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_ISSUE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToIssue), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_EMPLOYEE, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToEmployee), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToDepartment), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToClient), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedToSupplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(RelationItem.TYPE_CANDIDATE, new CellData(commonLocalizer.localize(PdfLocalizationName.reLatedCandidate), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CREATER, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.UPDATER, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.DURATION, new CellData(commonLocalizer.localize(PdfLocalizationName.duration), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CRM_ACCOUNT_RELATION, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedCrmAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CONTACT_RELATION, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedContact), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.LEAD_RELATION, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedLead), Element.ALIGN_LEFT));
        mapColumnHeader.put(EventItem.CANDIDATE_RELATION, new CellData(commonLocalizer.localize(PdfLocalizationName.reLatedCandidate), Element.ALIGN_LEFT));
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (EventItem item : eventItemListResult.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(EventItem.SUBJECT)) {
                mapColumns.put(EventItem.SUBJECT, getResultOrLongDash(item.getSubject()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.START_DATE)) {
                if (item.isAllDay()) {
                    mapColumns.put(EventItem.START_DATE, item.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate())) : dateFormat(item.getStartDate())) : "—");
                } else {
                    mapColumns.put(EventItem.START_DATE, item.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getStartDate())) : longDateFormat(item.getStartDate())) : "—");
                }
            }
            if (panelTools.getColumnCodeName().contains(EventItem.END_DATE)) {
                if (item.isAllDay()) {
                    mapColumns.put(EventItem.END_DATE, item.getEndDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())) : dateFormat(item.getEndDate())) : "—");
                } else {
                    mapColumns.put(EventItem.END_DATE, item.getEndDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getEndDate())) : longDateFormat(item.getEndDate())) : "—");
                }
            }
            if (panelTools.getColumnCodeName().contains(EventItem.VENUE)) {
                mapColumns.put(EventItem.VENUE, getResultOrLongDash(item.getLocation()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CRM_ACCOUNT_RELATION)) {
                mapColumns.put(EventItem.CRM_ACCOUNT_RELATION, getResultOrLongDash(item.getCrmAccountRelation().getToName()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CONTACT_RELATION)) {
                mapColumns.put(EventItem.CONTACT_RELATION, getResultOrLongDash(item.getContactRelation().getToName()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.LEAD_RELATION)) {
                mapColumns.put(EventItem.LEAD_RELATION, getResultOrLongDash(item.getLeadRelation().getToName()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CANDIDATE_RELATION)) {
                mapColumns.put(EventItem.CANDIDATE_RELATION, getResultOrLongDash(item.getCandidateRelation().getToName()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.DESCRIPTION)) {
                mapColumns.put(EventItem.DESCRIPTION, getResultOrLongDash(item.getDescription()));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CALL_TYPE)) {
                mapColumns.put(EventItem.CALL_TYPE, item.isCallLog() ? crmLocalizer.localize(PdfLocalizationName.call) : commonLocalizer.localize(PdfLocalizationName.event));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.EVENT_TYPE)) {
                mapColumns.put(EventItem.EVENT_TYPE, item.isCallLog() ? crmLocalizer.localize(PdfLocalizationName.call) : commonLocalizer.localize(PdfLocalizationName.event));
            }
            if (panelTools.getColumnCodeName().contains(EventItem.ASSIGNEE)) {
                String assigness = item.getSharedEmployeesString() != null && !"".equals(item.getSharedEmployeesString()) ? item.getSharedEmployeesString() : "—";
                mapColumns.put(EventItem.ASSIGNEE, assigness);
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CREATER)) {
                String creator = item.getCreatedBy() != null && !"".equals(item.getCreatedBy()) ? item.getCreatedBy() : "—";
                mapColumns.put(EventItem.CREATER, creator);
            }
            if (panelTools.getColumnCodeName().contains(EventItem.CREATED_DATE)) {
                mapColumns.put(EventItem.CREATED_DATE, item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : longDateFormat(item.getCreatedDate())) : "—");
            }
            if (panelTools.getColumnCodeName().contains(EventItem.UPDATER)) {
                mapColumns.put(EventItem.UPDATER, item.getLastModifiedBy() != null ? item.getLastModifiedBy() : "—");
            }
            if (panelTools.getColumnCodeName().contains(EventItem.UPDATED_DATE)) {
                mapColumns.put(EventItem.UPDATED_DATE, item.getLastModifiedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getLastModifiedDate())) : longDateFormat(item.getLastModifiedDate())) : "—");
            }
            if (panelTools.getColumnCodeName().contains(EventItem.DURATION)) {

                String duration = "";
                if (item != null && item.getCallDuration() > 0) {
                    if (item.getCallDuration() / 60 < 10) {
                        duration += "0";
                    }
                    duration += (int) item.getCallDuration() / 60 + ":";

                    if (item.getCallDuration() % 60 < 10) {
                        duration += "0";
                    }
                    duration += item.getCallDuration() % 60;
                }

                mapColumns.put(EventItem.DURATION, duration);
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CONTACT)) {
                mapColumns.put(RelationItem.TYPE_CONTACT, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_CONTACT) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_LEAD)) {
                mapColumns.put(RelationItem.TYPE_LEAD, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_LEAD) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CRM_ACCOUNT)) {
                mapColumns.put(RelationItem.TYPE_CRM_ACCOUNT, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CASE)) {
                mapColumns.put(RelationItem.TYPE_CASE, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_CASE) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_OPPORTUNITY)) {
                mapColumns.put(RelationItem.TYPE_OPPORTUNITY, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_PROJECT)) {
                mapColumns.put(RelationItem.TYPE_PROJECT, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_PROJECT) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_TASK)) {
                mapColumns.put(RelationItem.TYPE_TASK, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_TASK) : "—");
            }
            //related issue
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_ISSUE)) {
                mapColumns.put(RelationItem.TYPE_ISSUE, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_ISSUE) : "—");
            }
            //related employee
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_EMPLOYEE)) {
                mapColumns.put(RelationItem.TYPE_EMPLOYEE, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE) : "—");
            }
            //related department
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_DEPARTMENT)) {
                mapColumns.put(RelationItem.TYPE_DEPARTMENT, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT) : "—");
            }
            //related client
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CLIENT)) {
                mapColumns.put(RelationItem.TYPE_CLIENT, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_CLIENT) : "—");
            }
            //related supplier
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_SUPPLIER)) {
                mapColumns.put(RelationItem.TYPE_SUPPLIER, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER) : "—");
            }
            //related candidate
            if (panelTools.getColumnCodeName().contains(RelationItem.TYPE_CANDIDATE)) {
                mapColumns.put(RelationItem.TYPE_CANDIDATE, item.getRelationValueMap() != null ? item.getRelationValueMap().get(RelationItem.TYPE_CANDIDATE) : "—");
            }
            setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);

            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new String[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    public void setCustomFieldsPdfTableRows(List<CompanyCustomFieldItem> customfields, Map<String, String> pdfTableRows, List<String> fieldColumnCode, ListingCustomFields customFieldData, EdsCompany edsCompany) {
        if (customFieldData != null && customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                if (fieldColumnCode.contains(field.getColumnCode())) {
                    try {
                        if (Constants.DATA_TYPE_DATE.equals(field.getDataType())) {
                            Date data = (Date) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), Utils.formatDate(data, edsCompany));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), "—");
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(field.getDataType())) {
                            Double data = (Double) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), Utils.formatDouble(data));
                            } else {
                                pdfTableRows.put(field.getColumnCode(), "—");
                            }
                        } else {
                            String data = (String) customFieldData.getCustomFieldsValue(field.getColumnCode());
                            if (data != null) {
                                pdfTableRows.put(field.getColumnCode(), data);
                            } else {
                                pdfTableRows.put(field.getColumnCode(), "—");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
