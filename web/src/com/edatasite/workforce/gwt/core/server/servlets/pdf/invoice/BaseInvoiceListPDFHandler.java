package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class BaseInvoiceListPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;
    protected static final int SALE_INVOICE = 1;
    protected static final int SALE_QUOTE = 2;
    protected static final int SALE_ORDER = 3;
    protected static final int PURCHASE_INVOICE = 4;
    protected static final int PURCHASE_ORDER = 5;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    private static final DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    protected abstract int getWhat();

    protected abstract String getTitle();

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("salequote")) {
            return property != null ? property.getPlural() : commonLocalizer.localize("salesQuotes");
        } else if (fp.getPropertyCode().equals("saleorder")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("salesOrders");
        } else if (fp.getPropertyCode().equals("saleinvoice")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("salesInvoice");
        } else if (fp.getPropertyCode().equals("purchaseorder")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("purchaseorder");
        } else if (fp.getPropertyCode().equals("purchaseInvoice")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("purchaseinvoice");
        }
        return null;
    }

    private CellData[] getTableHeaders(ListingFilterParameter fp, Integer from) {

        EdsUser user = userManager.getUser();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currency = fs.getCurrency().getSymbol();
        currency = (currency != null && !"".equals(currency)) ? currency : fs.getCurrency().getName();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (from == SALE_INVOICE || from == PURCHASE_INVOICE) {
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.Qnumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.invoiceDate), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CLIENT, from == SALE_INVOICE ? new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.customer), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.ORIGINAL_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.dueAmount), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.PAID_AMOUNT, new CellData(accountingLocalizer.localize(PdfLocalizationName.paidAmount), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.poNumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.taxTotal), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.REFERENCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.QUOTE_NUMBER, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.quoteNo), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CLIENT_VAT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.vatNumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY, new CellData(commonLocalizer.localize(PdfLocalizationName.opportunity), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.MANAGER, new CellData(hrmsLocalizer.localizeAccounting(PdfLocalizationName.manager), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.subtotal), Element.ALIGN_RIGHT));
        } else if (from == SALE_ORDER || from == PURCHASE_ORDER) {
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.Qnumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new CellData(accountingLocalizer.localizeWithParam(PdfLocalizationName.date), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.validDate), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CLIENT, from == SALE_ORDER ? new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.MANAGER, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.manager), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.QUOTE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.quoteNumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.subtotal), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.taxTotal), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.REMAINING_BALANCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.remainingBalance), Element.ALIGN_RIGHT));
            if (from == SALE_ORDER) {
                mapColumnHeader.put(InvoiceList.PO_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.poNumber), Element.ALIGN_LEFT));
                mapColumnHeader.put(InvoiceList.REFERENCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
                mapColumnHeader.put(InvoiceList.OPPORTUNITY_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.opportunity), Element.ALIGN_LEFT));
            }

        } else if (from == SALE_QUOTE) {
            mapColumnHeader.put(InvoiceList.INVOICE_NUMBER, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.Qnumber, "Number"), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.INVOICE_DATE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.date), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.DUE_DATE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.RELATED_PROJECT, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.project), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CREATOR, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.MANAGER, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.manager), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.CURRENCY, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.currency), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.DUE_AMOUNT, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.quoteAmount), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.STATUS, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.status), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.SUB_TOTAL, new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.subtotal), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.TAX_TOTAL, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.taxTotal), Element.ALIGN_RIGHT));
            mapColumnHeader.put(InvoiceList.OPPORTUNITY_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.opportunity, "Opportunity"), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.PO_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.poNumber), Element.ALIGN_LEFT));
            mapColumnHeader.put(InvoiceList.REFERENCE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        }
        mapColumnHeader.put(InvoiceList.BASE_TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total) + " " + "(" + currency + ")", Element.ALIGN_RIGHT));
        mapColumnHeader.put(InvoiceList.NET_AMOUNT_TOTAL, new CellData(accountingLocalizer.localize(PdfLocalizationName.netAmount), Element.ALIGN_RIGHT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<CellData> header = new ArrayList<>();
        header.add(new CellData(accountingLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        for (String columnCode : panelTools.getColumnCodeName()) {
            if (mapColumnHeader.containsKey(columnCode)) {
                header.add(mapColumnHeader.get(columnCode));
            }
        }
        return header.toArray(new CellData[0]);
    }

    protected abstract InvoiceList getInvoiceData(ListingFilterParameter fp, ListLoadConfig config);

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;

        if (filterParameters.getStartDateNC() != null) {
            filterParameters.setStartDate(ServerUtils.parseFilterParameterDate(filterParameters.getStartDateNC()));
        }
        if (filterParameters.getEndDateNC() != null) {
            filterParameters.setEndDate(ServerUtils.parseFilterParameterDate(filterParameters.getEndDateNC()));
        }

        CellData[] headers = getTableHeaders(filterParameters, getWhat());
        ITextTableList tableList = new ITextTableList(headers.length);
        pdfData.setListTable(tableList);
        EdsUser user = uploadManager.getUser();

        ListLoadConfig config = new ListLoadConfig();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            config.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            config.setLimit(LIMIT_PDF_ROWS);
            filterParameters.setLimit(LIMIT_PDF_ROWS);
        }
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        InvoiceList invoiceList = getInvoiceData(filterParameters, config);
        List<NewInvoice> invoiceData = invoiceList.getList();
        tableList.addPdfTableHeader(headers);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        int count = 1;
        if (invoiceData != null && invoiceData.size() > 0) {
            for (NewInvoice data : invoiceData) {
                String counter = String.valueOf(count);
                String inv_number = getResultOrLongDash(data.getInvoiceNumber());
                String inv_date = "";
                String due_date = "";
                if (user.getCompany().getLocale() != null && "ru".equals(user.getCompany().getLocale())) {
                    Locale ruLocale = new Locale("ru", "RU");
                    SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                    inv_date = data.getInvoiceDate() != null ? ruDateFormat.format(data.getInvoiceDate().getNonConvertedDate()) : "";
                    inv_date = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(inv_date) : inv_date;
                    due_date = data.getDueDate() != null ? ruDateFormat.format(data.getDueDate().getNonConvertedDate()) : "";
                    due_date = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(due_date) : due_date;
                } else {
                    inv_date = data.getInvoiceDate() != null ? shortDateFormat.format(data.getInvoiceDate().getNonConvertedDate()) : "";
                    inv_date = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(inv_date) : inv_date;
                    due_date = data.getDueDate() != null ? shortDateFormat.format(data.getDueDate().getNonConvertedDate()) : "";
                    due_date = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(due_date) : due_date;
                }
                String client = getResultOrLongDash(data.getClientName());
                if (data.getPaidAmount() == null) {
                    data.setPaidAmount(AccountingConstants.ZERO);
                }

                BigDecimal dueAmount = data.getTotalInInvoiceCurrency().subtract(data.getPaidAmount() != null ? data.getPaidAmount() : BigDecimal.ZERO);
                BigDecimal paidAmount = data.getPaidAmount();
                BigDecimal prospectAmount = data.getTotalInInvoiceCurrency().subtract(data.getPaidAmount() != null ? data.getPaidAmount() : BigDecimal.ZERO);
                BigDecimal originalAmount = data.getTotalInInvoiceCurrency();

                if (data.isCreditNote()) {
                    dueAmount = dueAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(dueAmount) : dueAmount;
                    paidAmount = paidAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(paidAmount) : paidAmount;
                    prospectAmount = prospectAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(prospectAmount) : prospectAmount;
                    originalAmount = originalAmount.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ZERO.subtract(originalAmount) : originalAmount;
                }

                String due_amount = priceScaleFormat.format(dueAmount);
                String paid_amount = priceScaleFormat.format(paidAmount);
                String prospect_amount = priceScaleFormat.format(prospectAmount);
                String original_amount = priceScaleFormat.format(originalAmount);
                String subTotal = data.getSubtotal() != null ? priceScaleFormat.format(data.getSubtotal()) : BigDecimal.ZERO.toString();
                String taxTotal = priceScaleFormat.format(data.getTotalTaxes() != null ? data.getTotalTaxes().multiply(data.getExchageRate() != null ? data.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO);
                String currency = getResultOrLongDash(data.getCurrencyName());
                String quoteNumber = getResultOrLongDash(data.getQuoteNumber());
                String status = "";
                if (getStatusForShow(data.getStatus(), data) == null) {
                    status = data.getStatus() != null ? data.getStatus() : "—";
                } else {
                    status = getStatusForShow(data.getStatus(), data);
                }
                String project = getResultOrLongDash(data.getRelatedProjectName());
                String createdBy = (data.getCreator() != null && data.getCreator().getName() != null) ? data.getCreator().getName() : "—";
                String manager = (data.getCurrentApproverSelectItem() != null && data.getCurrentApproverSelectItem().getName() != null) ? data.getCurrentApproverSelectItem().getName() : "—";
                String currentApprover = data.getCurrentApproverSelectItem() != null ? getResultOrLongDash(data.getCurrentApproverSelectItem().getName()) : "—";

                Map<String, CellData> columnMap = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_NUMBER)) {
                    columnMap.put(InvoiceList.INVOICE_NUMBER, new CellData(inv_number, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.INVOICE_DATE)) {
                    columnMap.put(InvoiceList.INVOICE_DATE, new CellData(inv_date, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_DATE)) {
                    columnMap.put(InvoiceList.DUE_DATE, new CellData(due_date, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.RELATED_PROJECT)) {
                    columnMap.put(InvoiceList.RELATED_PROJECT, new CellData(project, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT)) {
                    columnMap.put(InvoiceList.CLIENT, new CellData(client, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUPPLIER)) {
                    columnMap.put(InvoiceList.SUPPLIER, new CellData(client, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.REMAINING_BALANCE)) {
                    columnMap.put(InvoiceList.REMAINING_BALANCE, new CellData(data.isProgressInvoicing() && data.getInvoicedAmount() != null ? numberFormat.format(data.getTotalInInvoiceCurrency().subtract(data.getInvoicedAmount())) : "", ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR)) {
                    columnMap.put(InvoiceList.CREATOR, new CellData(createdBy, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.MANAGER)) {
                    if (getWhat() == SALE_QUOTE) {
                        columnMap.put(InvoiceList.MANAGER, new CellData(currentApprover, Element.ALIGN_LEFT));
                    } else {
                        columnMap.put(InvoiceList.MANAGER, new CellData(manager, Element.ALIGN_LEFT));
                    }
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CURRENCY)) {
                    columnMap.put(InvoiceList.CURRENCY, new CellData(currency, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.DUE_AMOUNT)) {
                    if (getWhat() == SALE_ORDER || getWhat() == SALE_QUOTE || getWhat() == PURCHASE_ORDER) {
                        columnMap.put(InvoiceList.DUE_AMOUNT, new CellData(prospect_amount, Element.ALIGN_RIGHT));
                    } else {
                        columnMap.put(InvoiceList.DUE_AMOUNT, new CellData(due_amount, Element.ALIGN_RIGHT));
                    }
                }
                if (getWhat() == SALE_ORDER || getWhat() == SALE_QUOTE || getWhat() == SALE_INVOICE || getWhat() == PURCHASE_INVOICE) {
                    columnMap.put(InvoiceList.REFERENCE, new CellData(getResultOrLongDash(data.getReference()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY_NUMBER)) {
                    columnMap.put(InvoiceList.OPPORTUNITY, new CellData(data.getOpportunityNumber() != null ? data.getOpportunityNumber() : data.getOpportunity(), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PAID_AMOUNT)) {
                    columnMap.put(InvoiceList.PAID_AMOUNT, new CellData(paid_amount, Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.ORIGINAL_AMOUNT)) {
                    columnMap.put(InvoiceList.ORIGINAL_AMOUNT, new CellData(original_amount, Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.BASE_TOTAL)) {
                    columnMap.put(InvoiceList.BASE_TOTAL, new CellData(priceScaleFormat.format(data.getTotal()), Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.STATUS)) {
                    columnMap.put(InvoiceList.STATUS, new CellData(status, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.QUOTE_NUMBER)) {
                    columnMap.put(InvoiceList.QUOTE_NUMBER, new CellData(quoteNumber, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.OPPORTUNITY)) {
                    columnMap.put(InvoiceList.OPPORTUNITY, new CellData(data.getOpportunityNumber() != null ? data.getOpportunityNumber() : data.getOpportunity(), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.PO_NUMBER)) {
                    columnMap.put(InvoiceList.PO_NUMBER, new CellData(getResultOrLongDash(data.getPoNumber()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CREATOR) && (createdBy == null || "".equals(createdBy))) {
                    columnMap.put(InvoiceList.CREATOR, new CellData(getResultOrLongDash(data.getCreatorName()), Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.SUB_TOTAL)) {
                    columnMap.put(InvoiceList.SUB_TOTAL, new CellData(subTotal, Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.TAX_TOTAL)) {
                    columnMap.put(InvoiceList.TAX_TOTAL, new CellData(taxTotal, Element.ALIGN_RIGHT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.CLIENT_VAT_NUMBER)) {
                    String vatNumber = !ServerUtils.isNullOrEmpty(data.getClientTrnNumber()) ? data.getClientTrnNumber() : data.getClientVatNumber();
                    columnMap.put(InvoiceList.CLIENT_VAT_NUMBER, new CellData(vatNumber, Element.ALIGN_LEFT));
                }
                if (panelTools.getColumnCodeName().contains(InvoiceList.NET_AMOUNT_TOTAL)) {
                    columnMap.put(InvoiceList.NET_AMOUNT_TOTAL, new CellData(priceScaleFormat.format(data.getNetAmountTotal()), Element.ALIGN_RIGHT));
                }

                CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), columnMap, panelTools.getColumnCodeName(), data, company);

                List<CellData> column = new ArrayList<>();
                column.add(new CellData(counter, Element.ALIGN_LEFT));
                for (String columnCode : panelTools.getColumnCodeName()) {
                    if (columnMap.containsKey(columnCode)) {
                        column.add(columnMap.get(columnCode));
                    }
                }
                tableList.addPdfTableRows(column.toArray(new CellData[0]));

                count++;
            }
        }

        return pdfData;
    }

    protected String getStatusForShow(String status, NewInvoice data) {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(getFileName() + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }
}
