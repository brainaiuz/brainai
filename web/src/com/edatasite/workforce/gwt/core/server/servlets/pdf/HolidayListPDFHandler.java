package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
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
 * Time: 13:35:54
 * To change this template use File | Settings | File Templates.
 */
public class HolidayListPDFHandler extends AbstractITextPostPdfHandler {

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
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<HolidayItem> holidayList = availabilityService.getHolidays(filterParametrs);
        List<HolidayItem> holListItems = holidayList.getList();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(HolidayItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(HolidayItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(HolidayItem.FROM, new CellData(commonLocalizer.localize(PdfLocalizationName.from), Element.ALIGN_LEFT));
        mapColumnHeader.put(HolidayItem.TO, new CellData(commonLocalizer.localize(PdfLocalizationName.to), Element.ALIGN_LEFT));
        mapColumnHeader.put(HolidayItem.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (HolidayItem holidays : holListItems) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(HolidayItem.NAME)) {
                mapColumns.put(HolidayItem.NAME, getResultOrLongDash(holidays.getName()));
            }
            if (panelTools.getColumnCodeName().contains(HolidayItem.DESCRIPTION)) {
                mapColumns.put(HolidayItem.DESCRIPTION, getResultOrLongDash(holidays.getDescription()));
            }
            if (panelTools.getColumnCodeName().contains(HolidayItem.FROM)) {
                String fromDate = dateFormat(holidays.getFrom().getDate(), true);
                mapColumns.put(HolidayItem.FROM, holidays.getFrom() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(fromDate) : fromDate : "—");
            }
            if (panelTools.getColumnCodeName().contains(HolidayItem.TO)) {
                String toDate = dateFormat(holidays.getTo().getDate(), true);
                mapColumns.put(HolidayItem.TO, holidays.getTo() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(toDate) : toDate : "—");
            }

            if (panelTools.getColumnCodeName().contains(HolidayItem.LOCATION)) {
                mapColumns.put(HolidayItem.LOCATION, getResultOrLongDash(holidays.getLocationName()));
            }
            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(mapColumns::containsKey)
                    .map(mapColumns::get)
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
        return pdfWfmMessageSource.localize("holidayList");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_HolidayList_" + dateFormat(new Date()));
    }
}
