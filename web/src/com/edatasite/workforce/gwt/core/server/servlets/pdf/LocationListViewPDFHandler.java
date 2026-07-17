package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

public class LocationListViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    private LocationService locationService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer locationId = requestObject.getObjectID();
        CompLocationRpc item = locationService.getLocation(locationId);

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable locationTable = new CustomisedITextTable();
        locationTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        locationTable.addRowWithCode(HOME_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), escapeHtml(item.getCountryName()));
        locationTable.addRowWithCode(HOME_STATE, commonLocalizer.localize(PdfLocalizationName.state), escapeHtml(item.getStateName()));
        locationTable.addRowWithCode(HOME_CITY, commonLocalizer.localize(PdfLocalizationName.cityOnly), escapeHtml(item.getCityName()));
        locationTable.addRowWithCode(LOCATION_FAX, commonLocalizer.localize(PdfLocalizationName.fax), escapeHtml(item.getFax()));
        locationTable.addRowWithCode(EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(item.getEmail()));
        locationTable.addRowWithCode(PHONE, commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(item.getPhoneNumber()));
        locationTable.addRowWithCode(HOME_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), escapeHtml(item.getZipCode()));
        locationTable.addRowWithCode(NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(item.getName()));

        customData.put("LOCATION_TABLE", locationTable);
        pdfData.setCustomData(customData);
        return pdfData;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getName() + "_" + user.getLastName() + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.summaryOnly);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.LOCATION;
    }
}
