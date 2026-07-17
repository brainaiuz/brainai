package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
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
 * User: Xushnud
 * Date: 29.12.2009
 * Time: 18:01:36
 */
public class LocationListPDFHandler extends AbstractITextPostPdfHandler {

    private LocationService locationService;

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParameters.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<CompLocationRpc> locationList = locationService.getLocations(filterParameters);

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(CompLocationRpc.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.COUNTRY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.STATE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.state), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.CITY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.city), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.PHONE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.FAX, new CellData(commonLocalizer.localize(PdfLocalizationName.fax), Element.ALIGN_LEFT));
        columnHeaderMap.put(CompLocationRpc.ZIP_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.postCode), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (CompLocationRpc location : locationList.getList()) {
            Map<String, CellData> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.NAME)) {
                columnMap.put(CompLocationRpc.NAME, new CellData(getResultOrLongDash(location.getName())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.COUNTRY_NAME)) {
                columnMap.put(CompLocationRpc.COUNTRY_NAME, new CellData(getResultOrLongDash(location.getCountryName())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.STATE_NAME)) {
                columnMap.put(CompLocationRpc.STATE_NAME, new CellData(getResultOrLongDash(location.getStateName())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.CITY_NAME)) {
                columnMap.put(CompLocationRpc.CITY_NAME, new CellData(getResultOrLongDash(location.getCityName())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.EMAIL)) {
                columnMap.put(CompLocationRpc.EMAIL, new CellData(getResultOrLongDash(location.getEmail())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.PHONE_NUMBER)) {
                columnMap.put(CompLocationRpc.PHONE_NUMBER, new CellData(getResultOrLongDash(location.getPhoneNumber())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.FAX)) {
                columnMap.put(CompLocationRpc.FAX, new CellData(getResultOrLongDash(location.getFax())));
            }
            if (panelTools.getColumnCodeName().contains(CompLocationRpc.ZIP_CODE)) {
                columnMap.put(CompLocationRpc.ZIP_CODE, new CellData(getResultOrLongDash(location.getZipCode())));
            }

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), location, company);
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
        return commonLocalizer.localize(PdfLocalizationName.locationListTableName);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_LocationList_" + dateFormat(new Date()));
    }
}
