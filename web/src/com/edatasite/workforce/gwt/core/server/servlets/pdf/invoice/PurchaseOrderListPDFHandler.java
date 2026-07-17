package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 29.05.2009
 * Time: 20:37:35
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderListPDFHandler extends BaseInvoiceListPDFHandler {

    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    protected InvoiceList getInvoiceData(ListingFilterParameter fp, ListLoadConfig config) {
        return invoiceCircularResolver.getPurchaseOrderData(fp);
    }

    @Override
    protected int getWhat() {
        return PURCHASE_ORDER;
    }

    public String getFileName() {
        return "Purchase_Orders_List";
    }

    protected String getTitle() {
        return accountingLocalizer.localize(PdfLocalizationName.purchaseOrdersList);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        String shortDateFormat = Optional.ofNullable(companySettings.getShortDateFormat()).orElse("MMM dd, yyyy");

        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }

        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParameters.setLimit(LIMIT_PDF_ROWS);
        }

        InvoiceList invoiceList = quoteServiceLocal.getPurchaseOrderData(filterParameters);
        List<NewInvoice> quotes = invoiceList.getList();
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        HashMap<String, CellData> columnHeaderMap = new HashMap<>();

        List<CellData[]> list = new LinkedList<>();

        columnHeaderMap.put(InvoiceList.INVOICE_NUMBER, new CellData(commonLocalizer.localizeWithParam(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.INVOICE_DATE, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.date), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.DUE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.validDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.RELATED_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.DUE_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.QUOTE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.quoteNumber), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.MANAGER, new CellData(commonLocalizer.localize(PdfLocalizationName.manager), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.SUB_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.subtotal), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.BASE_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total) + " (" + currency + ")", Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.TAX_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.taxTotal), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.OPPORTUNITY, new CellData(commonLocalizer.localize(PdfLocalizationName.opportunity), Element.ALIGN_LEFT));
        columnHeaderMap.put(InvoiceList.REFERENCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), columnHeaderMap);

        ArrayList<CellData> header = new ArrayList<>();
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (columnHeaderMap.containsKey(columnCode)) {
                header.add(columnHeaderMap.get(columnCode));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        Integer calculationScale = getCalculationScale();
        for (NewInvoice item : quotes) {
            String number = "", client = "", status = "";
            String creator = item.getCreator() != null ? item.getCreator().getName() : "";
            String manager = item.getCurrentApproverSelectItem() != null ? item.getCurrentApproverSelectItem().getName() : "";
            if (item.getInvoiceNumber() != null) {
                number = item.getInvoiceNumber();
            }
            if (item.getClientName() != null) {
                client = item.getClientName();
            }

            BigDecimal taxTotal = item.getTotalTaxes() != null ? item.getTotalTaxes().multiply(item.getExchageRate() != null ? item.getExchageRate() : BigDecimal.ONE) : ZERO;
            BigDecimal amount = item.getTotalInInvoiceCurrency().setScale(calculationScale, BigDecimal.ROUND_HALF_UP);

            CellData[] cellData;
            Map<String, CellData> mapColumn = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                mapColumn.put(InvoiceList.INVOICE_NUMBER, new CellData(number, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                String invoiceDate = ServerUtils.dateFormat(item.getInvoiceDate() != null ? item.getInvoiceDate().getNonConvertedDate() : null, shortDateFormat);
                mapColumn.put(InvoiceList.INVOICE_DATE, new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(invoiceDate) : invoiceDate,
                        Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE)) {
                String dueDate = ServerUtils.dateFormat(item.getDueDate() != null ? item.getDueDate().getNonConvertedDate() : null, shortDateFormat);
                mapColumn.put(InvoiceList.DUE_DATE, new CellData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dueDate) : dueDate,
                        Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                mapColumn.put(InvoiceList.RELATED_PROJECT, new CellData(item.getRelatedProjectName() != null ? item.getRelatedProjectName() : "N/A",
                        Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                mapColumn.put(InvoiceList.CLIENT, new CellData(client, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                mapColumn.put(InvoiceList.CURRENCY, new CellData(item.getCurrencyName(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                mapColumn.put(InvoiceList.DUE_AMOUNT, new CellData(String.valueOf(item.getTotalInInvoiceCurrency()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                mapColumn.put(InvoiceList.STATUS, new CellData(item.getStatus(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.QUOTE_NUMBER)) {
                mapColumn.put(InvoiceList.QUOTE_NUMBER, new CellData(item.getQuoteNumber(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                mapColumn.put(InvoiceList.CREATOR, new CellData(String.valueOf(creator), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY)) {
                mapColumn.put(InvoiceList.OPPORTUNITY, new CellData(item.getOpportunityNumber(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                mapColumn.put(InvoiceList.MANAGER, new CellData(manager, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                mapColumn.put(InvoiceList.SUB_TOTAL, new CellData(item.getSubtotal() != null ? String.valueOf(item.getSubtotal()) : "", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                mapColumn.put(InvoiceList.BASE_TOTAL, new CellData(String.valueOf(item.getTotal()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                mapColumn.put(InvoiceList.TAX_TOTAL, new CellData(taxTotal.setScale(calculationScale, RoundingMode.HALF_UP).toString(), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(InvoiceList.REFERENCE)) {
                mapColumn.put(InvoiceList.REFERENCE, new CellData(item.getReference(), Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), item, user.getCompany());
            List<CellData> column = new ArrayList<>();
            for (String columnCode : panelTools.getColumnCodeName()) {
                if (mapColumn.containsKey(columnCode)) {
                    column.add(mapColumn.get(columnCode));
                }
            }
            tableList.addPdfTableRows(column.toArray(new CellData[0]));
        }
        pdfData.setListTable(tableList);
        return pdfData;

    }
}
