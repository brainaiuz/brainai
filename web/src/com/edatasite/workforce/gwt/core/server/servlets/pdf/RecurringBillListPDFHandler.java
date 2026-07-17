package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fathulla on 22.12.15.
 */

public class RecurringBillListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();
        Integer calculationScale = getCalculationScale();

        ListResult<RecurringInvoiceListItem> list = invoiceService.getRecurringBillData(filterParametrs);
        List<RecurringInvoiceListItem> holListItems = list.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        ITextTableList tableList = new ITextTableList(header.size() - 1);
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(RecurringInvoiceListItem.CLIENT, new CellData(accountingLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.AMOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(RecurringInvoiceListItem.BASE_AMOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.total) + "(" + currency + ")", Element.ALIGN_RIGHT));
        mapColumnHeader.put(RecurringInvoiceListItem.REPEATS, new CellData(accountingLocalizer.localize(PdfLocalizationName.repeats), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.NEXT_IVOICE_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.nextInvoiceDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.END_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.endDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(RecurringInvoiceListItem.RECURRENCE_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.recurrenceStatus), Element.ALIGN_LEFT));

        for (int i = 1; i < header.size(); i++) {
            header2.add(mapColumnHeader.get(header.get(i)));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (RecurringInvoiceListItem bill : holListItems) {
            String[] temp = new String[header.size() - 1];
            Integer[] tempAligment = new Integer[header.size()];
            for (int j = 1; j < header.size(); j++) {
                tempAligment[j] = Element.ALIGN_LEFT;
                if (RecurringInvoiceListItem.CLIENT.equals(header.get(j))) {
                    temp[j - 1] = bill.getClient() != null ? bill.getClient() : "";
                    tempAligment[j - 1] = Element.ALIGN_LEFT;
                } else if (RecurringInvoiceListItem.AMOUNT.equals(header.get(j))) {
                    temp[j - 1] = bill.getAmountInInvoiceCurrency() == null ? "" : bill.getAmountInInvoiceCurrency().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString();
                    tempAligment[j - 1] = Element.ALIGN_RIGHT;
                } else if (RecurringInvoiceListItem.BASE_AMOUNT.equals(header.get(j))) {
                    temp[j - 1] = bill.getAmount() == null ? "" : bill.getAmount().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString();
                    tempAligment[j - 1] = Element.ALIGN_RIGHT;
                } else if (RecurringInvoiceListItem.REPEATS.equals(header.get(j))) {
                    temp[j - 1] = bill.getRepeats() == null ? "" : bill.getRepeats();
                } else if (RecurringInvoiceListItem.NEXT_IVOICE_DATE.equals(header.get(j))) {
                    temp[j - 1] = bill.getNextInvoiceDate() == null ? "" : dateFormat(bill.getNextInvoiceDate());
                } else if (RecurringInvoiceListItem.END_DATE.equals(header.get(j))) {
                    temp[j - 1] = bill.getEndDate() == null ? "" : dateFormat(bill.getEndDate());
                } else if (RecurringInvoiceListItem.STATUS.equals(header.get(j))) {
                    String status = "";
                    if (DRAFT.equals(bill.getStatusCode())) {
                        status = "draft";
                    } else if (APPROVE.equals(bill.getStatusCode())) {
                        status = "approved";
                    } else if (OPEN.equals(bill.getStatusCode())) {
                        status = "sent";
                    }
                    temp[j - 1] = "Invoice will be " + status;
                } else if (RecurringInvoiceListItem.RECURRENCE_STATUS.equals(header.get(j))) {
                    temp[j - 1] = bill.getRecurrenceStatus() == null ? "" : bill.getRecurrenceStatus();
                }
            }
            tableList.addPdfTableRows(tempAligment, temp);
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("recurringBills");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ReccurringBillList_" + dateFormat(user.getUserDate()));
    }


}
