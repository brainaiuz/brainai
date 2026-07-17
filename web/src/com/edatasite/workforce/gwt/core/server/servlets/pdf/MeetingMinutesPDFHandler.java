package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 01.05.12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class MeetingMinutesPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    MeetingMinutesService meetingMinutesService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<MeetingMinutesItem> meetingList = meetingMinutesService.getMeetingMinutes(filterParametrs);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(MeetingMinutesItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(MeetingMinutesItem.LOCATION, propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location));
        mapColumnHeader.put(MeetingMinutesItem.TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(MeetingMinutesItem.MEETING_ID, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(MeetingMinutesItem.CALLED_BY, commonLocalizer.localize(PdfLocalizationName.calledBy));
        mapColumnHeader.put(MeetingMinutesItem.DATE, commonLocalizer.localize(PdfLocalizationName.startDate));
        mapColumnHeader.put(MeetingMinutesItem.END_DATE, commonLocalizer.localize(PdfLocalizationName.endDate));
        mapColumnHeader.put(MeetingMinutesItem.PREPARED_BY, commonLocalizer.localize(PdfLocalizationName.preparedBy));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new String[]{}));

        for (MeetingMinutesItem newsListItem : meetingList.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.NAME)) {
                mapColumns.put(MeetingMinutesItem.NAME, newsListItem.getName() != null ? newsListItem.getName() : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.LOCATION)) {
                mapColumns.put(MeetingMinutesItem.LOCATION, newsListItem.getLocation() != null ? newsListItem.getLocation() : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.TYPE)) {
                mapColumns.put(MeetingMinutesItem.TYPE, newsListItem.getType() != null ? newsListItem.getType().getName() : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.MEETING_ID)) {
                mapColumns.put(MeetingMinutesItem.MEETING_ID, newsListItem.getMeetingNumber() != null ? newsListItem.getMeetingNumber() : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.CALLED_BY)) {
                mapColumns.put(MeetingMinutesItem.CALLED_BY, newsListItem.getCalledBy() != null ? newsListItem.getCalledBy().getName() : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.DATE)) {
                mapColumns.put(MeetingMinutesItem.DATE, newsListItem.getStartdate() != null ? longDateFormat(newsListItem.getStartdate(), false) : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.END_DATE)) {
                mapColumns.put(MeetingMinutesItem.END_DATE, newsListItem.getEnddate() != null ? longDateFormat(newsListItem.getEnddate(), false) : "");
            } 
            if (panelTools.getColumnCodeName().contains(MeetingMinutesItem.PREPARED_BY)) {
                mapColumns.put(MeetingMinutesItem.PREPARED_BY, newsListItem.getPreparedBy() != null ? newsListItem.getPreparedBy().getName() : "");
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
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (property == null) {
            return commonLocalizer.localize("meetingMinutes");
        } else {
            return property.getPlural();
        }
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_MeetingMinutesList_" + dateFormat(new Date()));
    }
}
