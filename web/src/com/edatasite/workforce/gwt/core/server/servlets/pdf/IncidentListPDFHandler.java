package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ilhombek
 * Date: 16.08.2009
 * Time: 17:27:57
 */
public class IncidentListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private HrmsService hrmsService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        filterParametrs.setIncident(true);
        ListResult<PerformanceNoteItem> incidentList = hrmsService.getPerformanceNoteList(filterParametrs);
        List<PerformanceNoteItem> incidentItems = incidentList.getList();

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(PerformanceNoteItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.RELATED_TO, new CellData(commonLocalizer.localize(PdfLocalizationName.relatedTo), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.PERIOD, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.STATUS, new CellData(availabilityLocalizer.localize(PdfLocalizationName.incidentStatus), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.REPORTED_BY, new CellData(commonLocalizer.localize(PdfLocalizationName.reportedBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.RESOLVER, new CellData(commonLocalizer.localize(PdfLocalizationName.resolverOwner), Element.ALIGN_LEFT));
        columnHeaderMap.put(PerformanceNoteItem.PRIORITY, new CellData(commonLocalizer.localize(PdfLocalizationName.priority), Element.ALIGN_LEFT));


        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                                          .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                                          .map(columnCode -> columnHeaderMap.get(columnCode))
                                          .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        Map<String, CellData> columnMap = new HashMap<>();
        for (PerformanceNoteItem incident : incidentItems) {
            columnMap.clear();

            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.NAME)) {
                columnMap.put(PerformanceNoteItem.NAME, new CellData(getResultOrLongDash(incident.getName())));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.RELATED_TO)) {
                columnMap.put(PerformanceNoteItem.RELATED_TO, new CellData(getResultOrLongDash(incident.getRelatedToName())));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.PERIOD)) {
                String formattedDate = "";
                if (incident.getStartDate() != null) {

                    formattedDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(incident.getStartDate().getNonConvertedDate())) : dateFormat(incident.getStartDate().getNonConvertedDate());
                }
                if (incident.getEndDate() != null) {
                    formattedDate += " - " + (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(incident.getEndDate().getNonConvertedDate())) : dateFormat(incident.getEndDate().getNonConvertedDate()));
                }
                columnMap.put(PerformanceNoteItem.PERIOD, new CellData(getResultOrLongDash(formattedDate)));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.STATUS)) {
                columnMap.put(PerformanceNoteItem.STATUS, new CellData(getResultOrLongDash(incident.getStatusName())));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.REPORTED_BY)) {
                columnMap.put(PerformanceNoteItem.REPORTED_BY, new CellData(getResultOrLongDash(incident.getReportedByName())));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.RESOLVER)) {
                columnMap.put(PerformanceNoteItem.RESOLVER, new CellData(getResultOrLongDash(incident.getResolverName())));
            }
            if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.PRIORITY)) {
                columnMap.put(PerformanceNoteItem.PRIORITY, new CellData(getResultOrLongDash(incident.getPriorityName())));
            }
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
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_IncidentList_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localizeWithParam(PdfLocalizationName.incidents);

    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }
}
