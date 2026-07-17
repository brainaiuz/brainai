package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ozodbek on 11/21/2017.
 */
public class InvoicePaymentListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {

    @Autowired
    private PrepaymentService prepaymentService;

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("supplierPrepayment")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("supplierPrepayment");
        } else if (fp.getPropertyCode().equals("customerPrepayment")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("customerPrepayment");
        }
        return null;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        boolean isReceivable = filterParameters.getListPanelTool().getType().getListName().contains("Prepayment");

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();

        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParameters.setLimit(MAX_PDF_OR_EXCEL_LIMIT);
        }

        filterParameters.setStartDate(parseFilterParameterDate(filterParameters.getStartDateNC()));
        filterParameters.setEndDate(parseFilterParameterDate(filterParameters.getEndDateNC()));
        filterParameters.setFromExcelPDF(true);

        ListResult<PrePaymentListItem> prePaymentList = prepaymentService.getPrePaymentList(filterParameters);

        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        ArrayList<String> columnCodeNames = panelTools.getColumnCodeName();
        ArrayList<CellData> headers = new ArrayList<>();
        columnCodeNames.remove(ACTION);
        ITextTableList tableList = new ITextTableList(columnCodeNames.size());
        pdfData.setListTable(tableList);
        LinkedHashMap<String, CellData> mapColumnHeader = new LinkedHashMap<>();
        mapColumnHeader.put(NUMBER_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(NOTE, new CellData(commonLocalizer.localize(PdfLocalizationName.note), Element.ALIGN_LEFT));
        mapColumnHeader.put(DATE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(PROJECT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(REFERENCE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        mapColumnHeader.put(AMOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(CURRENCY_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(STATUS_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.CUSTOMER, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.REMAINING_BALANCE, new CellData(accountingLocalizer.localize(PdfLocalizationName.remainingBalance), Element.ALIGN_LEFT));
        mapColumnHeader.put(SALE_QUOTE, new CellData(commonLocalizer.localize(PdfLocalizationName.salesQuote), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(CODE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.PAY_ACCOUNT, new CellData(commonLocalizer.localize(isReceivable ? PdfLocalizationName.paidTo : PdfLocalizationName.paidFrom), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.PURCHASE_ORDER, new CellData(commonLocalizer.localize(PdfLocalizationName.purchaseOrder), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        mapColumnHeader.put(PrePaymentListItem.SALE_INVOICE, new CellData(commonLocalizer.localize(PdfLocalizationName.salesInvoice), Element.ALIGN_LEFT));

        for (String aHeader : columnCodeNames) {
            headers.add(mapColumnHeader.get(aHeader));
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        tableList.addPdfTableHeader(headers.toArray(new CellData[0]));
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        for (PrePaymentListItem item : prePaymentList.getList()) {
            String[] temp = new String[columnCodeNames.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < columnCodeNames.size(); j++) {
                switch (columnCodeNames.get(j)) {
                    case NUMBER_COLUMN -> {
                        temp[j] = item.getNumber() != null ? item.getNumber() : "—";
                        cell.add(columnCodeNames.indexOf(NUMBER_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case DATE_COLUMN -> {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp[j] = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? ServerUtils.convertToUzbDateFormat(shortDateFormat.format(item.getDate().getNonConvertedDate())) : "—";
                            cell.add(columnCodeNames.indexOf(DATE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                        } else {
                            temp[j] = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getDate().getNonConvertedDate()) : "—";
                            cell.add(columnCodeNames.indexOf(DATE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                        }
//                        temp[j] = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getDate().getNonConvertedDate()) : "—";
//                        cell.add(columnCodeNames.indexOf(DATE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case PROJECT_COLUMN -> {
                        temp[j] = item.getProject() != null ? item.getProject() : "—";
                        cell.add(columnCodeNames.indexOf(PROJECT_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case REFERENCE_COLUMN -> {
                        temp[j] = item.getReference() != null ? item.getReference() : "—";
                        cell.add(columnCodeNames.indexOf(REFERENCE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case AMOUNT_COLUMN -> {
                        temp[j] = priceScaleFormat.format(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
                        cell.add(columnCodeNames.indexOf(AMOUNT_COLUMN), new CellData(temp[j], Element.ALIGN_RIGHT));
                    }
                    case CURRENCY_COLUMN -> {
                        temp[j] = item.getCurrency() != null ? item.getCurrency() : fs.getCurrency() != null ? fs.getCurrency().getName() : "—";
                        cell.add(columnCodeNames.indexOf(CURRENCY_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case STATUS_COLUMN -> {
                        temp[j] = AccountingConstants.PRE_PAYMENT_APPLIED_STATUS.equals(item.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.applied)
                                : AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(item.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.open)
                                : AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS.equals(item.getStatus()) ? commonLocalizer.localize(PdfLocalizationName.partialApplied) : "—";
                        cell.add(columnCodeNames.indexOf(STATUS_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case "remainingBalance" -> {
                        temp[j] = item.getRemainingBalance() != null ? priceScaleFormat.format(item.getRemainingBalance()) : "—";
                        cell.add(columnCodeNames.indexOf("remainingBalance"), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case SALE_QUOTE -> {
                        temp[j] = item.getSaleQuote() != null ? item.getSaleQuote().getName() : "—";
                        cell.add(columnCodeNames.indexOf(SALE_QUOTE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case CREATOR_COLUMN -> {
                        temp[j] = item.getCreator() != null ? item.getCreator() : "—";
                        cell.add(columnCodeNames.indexOf(CREATOR_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case CODE_COLUMN -> {
                        temp[j] = item.getAccountNumber() != null ? item.getAccountNumber() : "—";
                        cell.add(columnCodeNames.indexOf(CODE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case PAY_ACCOUNT_COLUMN -> {
                        temp[j] = item.getPayAccount() != null ? item.getPayAccount() : "—";
                        cell.add(columnCodeNames.indexOf(PAY_ACCOUNT_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case NOTE -> {
                        temp[j] = item.getNote() != null ? item.getNote() : "—";
                        cell.add(columnCodeNames.indexOf(NOTE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case SUPPLIER_COLUMN -> {
                        temp[j] = item.getCustomerName() != null ? item.getCustomerName() : "—";
                        cell.add(columnCodeNames.indexOf(SUPPLIER_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case CUSTOMER_COLUMN -> {
                        temp[j] = item.getCustomerName() != null ? item.getCustomerName() : "—";
                        cell.add(columnCodeNames.indexOf(CUSTOMER_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case PURCHASE_ORDER -> {
                        temp[j] = item.getPurchaseOrder() != null ? item.getPurchaseOrder().getName() : "—";
                        cell.add(columnCodeNames.indexOf(PURCHASE_ORDER), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case DEPARTMENT -> {
                        temp[j] = item.getDepartment() != null ? item.getDepartment() : "—";
                        cell.add(columnCodeNames.indexOf(DEPARTMENT), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    case SALE_INVOICE -> {
                        temp[j] = item.getSaleInvoice() != null ? item.getSaleInvoice().getName() : "—";
                        cell.add(columnCodeNames.indexOf(SALE_INVOICE), new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                    default -> {
                    }
                }

            }
            tableList.addPdfTableRows(cell.toArray(new CellData[columnCodeNames.size()]));
        }

        pdfData.setListTable(tableList);

        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        setFileName(filterParameters.getViewType() + dateFormat(user.getUserDate()));
    }
}
