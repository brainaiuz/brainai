package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Fathulla on 13.11.15.
 */
public class PaidBilsListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private InvoiceService invoiceService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize("paidBills");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        filterParametrs.setAllByFilter(false);
        filterParametrs.setDataType(Constants.PAYABLE);
        ListLoadConfig config = new ListLoadConfig();

        config.setSortField(filterParametrs.getSortField());

        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();

        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
            config.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
            config.setLimit(LIMIT_PDF_ROWS);
        }

        ListResult<BatchPaymentListItem> list = invoiceService.getBatchPayments(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> mapColumnHeader = getColumnHeaderMap();
        List<CellData> header = new ArrayList<>();

        for (String columnCode : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnCode)) {
                header.add(mapColumnHeader.get(columnCode));
            }
        }

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));
        pdfData.setListTable(tableList);

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (BatchPaymentListItem item : list.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();

            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.NUMBER)) {
                mapColumns.put(BatchPaymentListItem.NUMBER, new CellData(getResultOrLongDash(item.getNumber()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.CRM_ACCOUNT)) {
                mapColumns.put(BatchPaymentListItem.CRM_ACCOUNT, item.getCrmAccount() != null ? new CellData(getResultOrLongDash(item.getCrmAccount().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.DATE)) {
                mapColumns.put(BatchPaymentListItem.DATE, item.getDate() != null ? new CellData(dateFormat(item.getDate().getDate()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.REFERENCE)) {
                mapColumns.put(BatchPaymentListItem.REFERENCE, new CellData(getResultOrLongDash(item.getReference()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.ACCOUNT)) {
                mapColumns.put(BatchPaymentListItem.ACCOUNT, item.getAccount() != null ? new CellData(getResultOrLongDash(item.getAccount().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.AMOUNT)) {
                mapColumns.put(BatchPaymentListItem.AMOUNT, item.getTotalAmount() != null ? new CellData(priceScaleNumberFormat.format(item.getTotalAmount()), Element.ALIGN_RIGHT) : new CellData("0.00", Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.PAYMENT_TYPE)) {
                mapColumns.put(BatchPaymentListItem.PAYMENT_TYPE, item.getPaymentMethod() != null ? new CellData(getResultOrLongDash(item.getPaymentMethod().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(BatchPaymentListItem.CURRENCY)) {
                mapColumns.put(BatchPaymentListItem.CURRENCY, item.getCurrency() != null ? new CellData(getResultOrLongDash(item.getCurrency().getName())) : new CellData("—", Element.ALIGN_LEFT));
            }

            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }
        return pdfData;
    }

    private String refactor(String value) {
        return value != null && !"".equals(value) ? value : "N/A";
    }


    public Map<String, CellData> getColumnHeaderMap() {
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BatchPaymentListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.CRM_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.REFERENCE, new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(BatchPaymentListItem.PAYMENT_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentType), Element.ALIGN_LEFT));
        mapColumnHeader.put(BatchPaymentListItem.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));

        return mapColumnHeader;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Paid_Bils_List_" + dateFormat(new Date()));
    }


}
