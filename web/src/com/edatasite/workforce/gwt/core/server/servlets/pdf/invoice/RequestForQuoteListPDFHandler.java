package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dilshod on 1/24/2016.
 */
public class RequestForQuoteListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    QuoteService quoteService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("requestForQuote");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        String shortDateFormat = "MMM dd, yyyy";
        String dateAndTimeFormatShort2 = "MMM dd yyyy, HH:mm";
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        filterParameter.setLimit(LIMIT_PDF_ROWS);
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
            if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
                filterParameter.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
            }
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        ArrayList<CellData> header2 = new ArrayList<>();
        header.remove("action");

        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        HashMap<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(RFQData.REQUEST_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.REQUEST_FROM, new CellData(commonLocalizer.localizeWithParam(PdfLocalizationName.requestFrom, accountingLocalizer.localize(PdfLocalizationName.invoice2)), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.requestDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.VALID_UNTIL, new CellData(accountingLocalizer.localize(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.OPPORTUNITY_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.opportunity) + "#", Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.OPPORTUNITY_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        mapColumnHeader.put(RFQData.CUSTOMER_COUNTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));


        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[0]));

        ListResult<RFQData> result = quoteService.getRFQList(filterParameter);
        for (RFQData item : result.getList()) {
            String[] temp = new String[header.size()];
            for (int j = 0; j < header.size(); j++) {
                switch (header.get(j)) {
                    case RFQData.REQUEST_NUMBER ->
//                        temp[j] = item.getNumberData() != null ? item.getNumberData().getNumberString() : "";
                            temp[j] = item.getNumber() != null ? item.getNumber() : "";
                    case RFQData.REQUEST_FROM ->
                            temp[j] = COMPANY_SUPPLIERS.equals(item.getRequestFrom()) ? accountingLocalizer.localize(PdfLocalizationName.companySuppliers) : accountingLocalizer.localize(PdfLocalizationName.directorySuppliers);
                    case RFQData.DATE -> {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp[j] = ServerUtils.convertToUzbDateFormat(ServerUtils.longDateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, company));
                        } else {
                            temp[j] = ServerUtils.longDateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, company);
                        }
//                        temp[j] = ServerUtils.dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, dateAndTimeFormatShort2);
                    }
                    case RFQData.VALID_UNTIL -> {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp[j] = ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getValidUntil() != null ? item.getValidUntil().getNonConvertedDate() : null, shortDateFormat));
                        } else {
                            temp[j] = ServerUtils.dateFormat(item.getValidUntil() != null ? item.getValidUntil().getNonConvertedDate() : null, shortDateFormat);
                        }
//                        temp[j] = ServerUtils.dateFormat(item.getValidUntil() != null ? item.getValidUntil().getNonConvertedDate() : null, shortDateFormat);

                    }
                    case RFQData.STATUS -> temp[j] = getStatusName(item.getOverallStatus().getCode());
                    case RFQData.OPPORTUNITY_NUMBER ->
                            temp[j] = item.getOppportunityNumber() != null ? item.getOppportunityNumber() : "";
                    case RFQData.CUSTOMER_COUNTRY ->
                            temp[j] = item.getClientAddress() != null ? item.getClientAddress() : "";
                    case RFQData.OPPORTUNITY_NAME ->
//                        temp[j] = item.getOpportunityName() != null ? item.getOpportunityName() : "";
                            temp[j] = item.getCustomer() != null ? item.getCustomer().getName() : "";
                    case RFQData.PROJECT -> temp[j] = item.getProject() != null ? item.getProject().getName() : "";
                    case RFQData.APPROVER -> temp[j] = item.getApprover() != null ? item.getApprover().getName() : "";
                    default -> {
                        if (item.getCustomFields() != null && item.getCustomFields().get(header.get(j)) != null) {
                            if (item.getCustomFields().get(header.get(j)) instanceof Date) {
                                temp[j] = dateFormat((Date) item.getCustomFields().get(header.get(j)));
                            } else {
                                temp[j] = item.getCustomFields().get(header.get(j)) != null ? item.getCustomFields().get(header.get(j)).toString() : "";
                            }
                        }
                    }
                }
            }
            tableList.addPdfTableRows(temp);
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    private String getStatusName(String status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case Constants.RFQ_CONVERTED -> accountingLocalizer.localize(PdfLocalizationName.converted, "Converted");
            case Constants.RFQ_PARTIAL_CONVERTED -> "Partially Converted";
            case Constants.RFQ_DRAFT -> accountingLocalizer.localize(PdfLocalizationName.draft, "Draft");
            case Constants.RFQ_SUBMITTED -> commonLocalizer.localize(PdfLocalizationName.submitted, "Submitted");
            case Constants.RFQ_APPROVED -> commonLocalizer.localize(PdfLocalizationName.approved, "Approved");
            case Constants.RFQ_DECLINED -> commonLocalizer.localize(PdfLocalizationName.rejected, "Rejected");
            default -> status;
        };
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Request_For_Quote");

    }
}
