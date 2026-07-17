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
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by dilshod on 1/18/2016.
 */
public class BatchPaymentListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private InvoiceService invoiceService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        if (filterParametrs.getPropertyCode().equals("BATCH_RECEIVE_PAYMENT")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("ACCOUNTING_RECEIVE_PAYMENT_LIST");
        } else if (filterParametrs.getPropertyCode().equals("payBillsList")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("payInvoices");
        }
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        String shortDateFormat = "MMM dd, yyyy";
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(LIMIT_PDF_ROWS);
        boolean isReceivable = Constants.RECEIVABLE.equals(filterParametrs.getDataType());
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
            if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
                filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
            }
        }

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(BatchPaymentListItem.ACTION);

        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BatchPaymentListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.CRM_ACCOUNT, isReceivable ? new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.REFERENCE, new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(BatchPaymentListItem.PAYMENT_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentType), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[0]));

        Integer calculationScale = getCalculationScale();
        ListResult<BatchPaymentListItem> result = invoiceService.getBatchPayments(filterParametrs);
        for (BatchPaymentListItem item : result.getList()) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                switch (header.get(j)) {
                    case BatchPaymentListItem.NUMBER -> {
                        temp[j] = getResultOrLongDash(item.getNumber());
                        cell.add(header.indexOf(BatchPaymentListItem.NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.CRM_ACCOUNT -> {
                        temp[j] = item.getCrmAccount() != null && item.getCrmAccount().getName() != null ? item.getCrmAccount().getName() : "—";
                        cell.add(header.indexOf(BatchPaymentListItem.CRM_ACCOUNT), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.DATE -> {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp[j] = item.getDate() != null ? ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getDate().getNonConvertedDate(), shortDateFormat)) : "—";
                            cell.add(header.indexOf(BatchPaymentListItem.DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                        } else {
                            temp[j] = item.getDate() != null ? ServerUtils.dateFormat(item.getDate().getNonConvertedDate(), shortDateFormat) : "—";
                            cell.add(header.indexOf(BatchPaymentListItem.DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                        }
//                        temp[j] = item.getDate() != null ? ServerUtils.dateFormat(item.getDate().getNonConvertedDate(), shortDateFormat) : "—";
//                        cell.add(header.indexOf(BatchPaymentListItem.DATE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.REFERENCE -> {
                        temp[j] = getResultOrLongDash(item.getReference());
                        cell.add(header.indexOf(BatchPaymentListItem.REFERENCE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.ACCOUNT -> {
                        temp[j] = item.getAccount() != null && item.getAccount().getName() != null ? item.getAccount().getName() : "—";
                        cell.add(header.indexOf(BatchPaymentListItem.ACCOUNT), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.AMOUNT -> {
                        temp[j] = "" + (item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP);
                        cell.add(header.indexOf(BatchPaymentListItem.AMOUNT), new CellData(temp[j], Element.ALIGN_RIGHT));
                    }
                    case BatchPaymentListItem.PAYMENT_TYPE -> {
                        temp[j] = item.getPaymentMethod() != null && item.getPaymentMethod().getName() != null ? item.getPaymentMethod().getName() : "—";
                        cell.add(header.indexOf(BatchPaymentListItem.PAYMENT_TYPE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.CURRENCY -> {
                        temp[j] = item.getCurrency() != null && item.getCurrency().getName() != null ? item.getCurrency().getName() : "—";
                        cell.add(header.indexOf(BatchPaymentListItem.CURRENCY), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.PROJECT -> {
                        temp[j] = getResultOrLongDash(item.getProject());
                        cell.add(header.indexOf(BatchPaymentListItem.PROJECT), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case BatchPaymentListItem.CREATOR -> {
                        temp[j] = getResultOrLongDash(item.getCreator());
                        cell.add(header.indexOf(BatchPaymentListItem.CREATOR), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    default -> {
                        if (item.getCustomFieldsMap() != null) {
                            if (item.getCustomFieldsValue(header.get(j)) instanceof Date) {
                                temp[j] = dateFormat((Date) item.getCustomFieldsValue(header.get(j)));
                                cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                            } else {
                                temp[j] = item.getCustomFieldsValue(header.get(j)) != null ? item.getCustomFieldsValue(header.get(j)).toString() : "—";
                                cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                            }
                        }
                    }
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        setFileName(Constants.RECEIVABLE.equals(filterParametrs.getDataType()) ? "Invoice_Payments" : "Paid_Bills");
    }
}
