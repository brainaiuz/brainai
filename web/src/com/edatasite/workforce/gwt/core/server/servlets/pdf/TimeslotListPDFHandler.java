package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 16.08.2009
 * Time: 16:04:58
 * To change this template use File | Settings | File Templates.
 */

public class TimeslotListPDFHandler extends AbstractITextPostPdfHandler {

    private AvailabilityService availabilityService;

    public void setAvailabilityService(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
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

        ListResult<TimeslotItem> timeslotItemListResult = availabilityService.getTimeslots(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TimeslotItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeslotItem.DEPARTMENTS, new CellData(commonLocalizer.localize(PdfLocalizationName.departments), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeslotItem.SHORT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.shortName), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeslotItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (TimeslotItem item : timeslotItemListResult.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(TimeslotItem.NAME)) {
                mapColumns.put(TimeslotItem.NAME, getResultOrLongDash(item.getName()));
            }
            if (panelTools.getColumnCodeName().contains(TimeslotItem.DEPARTMENTS)) {
                mapColumns.put(TimeslotItem.DEPARTMENTS, getResultOrLongDash(item.getDepartmentsAsString()));
            }
            if (panelTools.getColumnCodeName().contains(TimeslotItem.SHORT_NAME)) {
                mapColumns.put(TimeslotItem.SHORT_NAME, getResultOrLongDash(item.getShortName()));
            }
            if (panelTools.getColumnCodeName().contains(TimeslotItem.DESCRIPTION)) {
                mapColumns.put(TimeslotItem.DESCRIPTION, getResultOrLongDash(item.getDescription()));
            }

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

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("timeslotList");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_TimeslotList_" + dateFormat(new Date()));
    }
}
