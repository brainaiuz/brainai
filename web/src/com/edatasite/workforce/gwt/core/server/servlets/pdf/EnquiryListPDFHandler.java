package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.EnquiryService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/30/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

public class EnquiryListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private EnquiryService enquiryService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
        }
        ListResult<EnquiryItem> studentList = enquiryService.geEnquiryList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<EnquiryItem> enquiryItems = studentList.getList();
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.enquiryListFileName) + "_" + dateFormat(new Date()));
        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.enquiryListTableName, user.getFullName()));

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(EnquiryItem.ENQUIRY_MODE, new CellData(commonLocalizer.localize(PdfLocalizationName.enquiryMode), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.ENQUIRY_CUSTOMER, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.ENQUIRY_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.enquiryDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.CUSTOMER_CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.CONTACT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.contactName), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.CONTACT_EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        columnHeaderMap.put(EnquiryItem.REF_INFO, new CellData(commonLocalizer.localize(PdfLocalizationName.refInfo), Element.ALIGN_LEFT));

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        if (enquiryItems != null) {
            for (EnquiryItem enquiry : enquiryItems) {
                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_MODE)) {
                    columnMap.put(EnquiryItem.ENQUIRY_MODE, enquiry.getEnquiryMode() != null ? new CellData(getResultOrLongDash(enquiry.getEnquiryMode().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_CUSTOMER)) {
                    columnMap.put(EnquiryItem.ENQUIRY_CUSTOMER, enquiry.getCustomer() != null ? new CellData( getResultOrLongDash(enquiry.getCustomer().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.ENQUIRY_DATE)) {
                    columnMap.put(EnquiryItem.ENQUIRY_DATE, enquiry.getEnquiryDate() != null ? new CellData(dateFormat(enquiry.getEnquiryDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CUSTOMER_CURRENCY)) {
                    columnMap.put(EnquiryItem.CUSTOMER_CURRENCY, enquiry.getCustomer().getId() != null ? new CellData(getResultOrLongDash(enquiry.getCustomer().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CONTACT_NAME)) {
                    columnMap.put(EnquiryItem.CONTACT_NAME, enquiry.getContactDetails() != null ? new CellData(getResultOrLongDash(enquiry.getContactDetails().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.CONTACT_EMAIL)) {
                    columnMap.put(EnquiryItem.CONTACT_EMAIL, enquiry.getContactDetails() != null ? new CellData(getResultOrLongDash(enquiry.getContactDetails().getPrimaryEmail()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(EnquiryItem.REF_INFO)) {
                    columnMap.put(EnquiryItem.REF_INFO, new CellData(getResultOrLongDash(enquiry.getRefInfo()), Element.ALIGN_LEFT));
                }
                List<CellData> column = new ArrayList<>();
                for (String columnCode : panelTools.getColumnCodeName()) {
                    if (columnMap.containsKey(columnCode)) {
                        column.add(columnMap.get(columnCode));
                    }
                }
                tableList.addPdfTableRows(column.toArray(new CellData[0]));
            }
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.enquiryListFileName) + "_" + dateFormat(new Date()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("enquiryList");
    }
}
