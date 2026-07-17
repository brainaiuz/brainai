package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 04-Jun-2009
 * Time: 08:54:45
 * To change this template use File | Settings | File Templates.
 */
public class JournalReportPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        boolean isDepartmentRelationEnabled = filterParametrs.getDepartmentId() != null;
        filterParametrs.setFromExcelPDF(true);

        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        String startDateValue = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(startDate)) : format.format(startDate);
        String endDateValue = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(endDate)) : format.format(endDate);
        String date = commonLocalizer.localize(PdfLocalizationName.from) + " " +
                " " + startDateValue + " - " + commonLocalizer.localize(PdfLocalizationName.to) + " " + endDateValue;
        pdfData.setCurrentDate(date);
        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")"));

        DateNonConvertable fromDate = new DateNonConvertable(ServerUtils.getDayStartTime(startDate));
        DateNonConvertable toDate = new DateNonConvertable(ServerUtils.getDayEndTime(endDate));
        ITextTableList table = new ITextTableList(4);
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.account));
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.department));
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.debit));
        table.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.credit));
        pdfData.setListTable(table);

        ListingResult<Transaction> transactions = accountingService.getJournalReportWithPaging(fromDate, toDate, filterParametrs.getSortField(), filterParametrs.getJournalID(), filterParametrs);
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        if (transactions != null && transactions.getList() != null) {
            HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
            for (Transaction transaction : transactions.getList()) {
                customData.put(transaction.getJournalId().toString(), getJournalTable(transaction, priceScaleNumberFormat, format, isDepartmentRelationEnabled));
            }
            pdfData.setCustomData(customData);
        }

        return pdfData;
    }

    private CustomisedITextTable getJournalTable(Transaction transaction, DecimalFormat decimalFormat, SimpleDateFormat format, boolean isDepartmentRelationEnabled) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setHeader(getHeader(transaction, format));
        int i = 0;
        for (TransactionItem item : transaction.getTransactionItems()) {
            LinkedList<String> totalRows = new LinkedList<>();
            totalRows.add(StringUtils.join(item.getAccountName(), "<small>", "(", item.getAccountCode(), ")", "</small>"));
            if (isDepartmentRelationEnabled) {
                totalRows.add(item.getDepartment() != null ? item.getDepartment() : "");
            } else {
                totalRows.add("");
            }
            totalRows.add(getValueAsString(item.getDebit(), decimalFormat));
            totalRows.add(getValueAsString(item.getCredit(), decimalFormat));
            table.addTotalRow("" + i++, totalRows);
        }
        table.addTotalRow("TOTAL", getTotal(transaction, decimalFormat, isDepartmentRelationEnabled));
        return table;
    }

    private Map<String, String> getHeader(Transaction transaction, SimpleDateFormat format) {
        Map<String, String> header = new HashMap<>();
        header.put("ID", accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.wfmJournalIDdata, "" + transaction.getJournalId()));

        String formatedDate = transaction.getPostedDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(transaction.getPostedDate().getNonConvertedDate())) : format.format(transaction.getPostedDate().getNonConvertedDate()) : "";
        String postedBy = accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.postedBy, Constants.MANUAL_TRANSACTION.equals(transaction.getTransactionType()) ? "Manual Journal: " : "",
                transaction.getPostedBy(), formatedDate);
        String reversed = transaction.getReversedJournalId() != null ? accountingLocalizer.localizeAccounting(PdfLocalizationName.reversed) : "";
        String transactionType = INVOICEPAYMENT_TRANSACTION.equals(transaction.getTransactionType()) ? transaction.getJournalName().replaceAll("\\bnull\\b", accountingLocalizer.localizeAccounting(PdfLocalizationName.vatRefund)) : transaction.getJournalName();
        String reversalJournalID = transaction.getReversedJournalId() != null ? accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.reversalOf, "" + transaction.getReversedJournalId()) : "";
        header.put("NAME", StringUtils.join(reversed, " ", transactionType, " ", "<small>", postedBy, "</small>", " ", reversalJournalID));

        header.put("DATE", transaction.getJournalDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(transaction.getJournalDate().getNonConvertedDate())) : format.format(transaction.getJournalDate().getNonConvertedDate()) : "");
        return header;
    }

    private LinkedList<String> getTotal(Transaction transaction, DecimalFormat decimalFormat, boolean isDepartmentRelationEnabled) {
        LinkedList<String> total = new LinkedList<>();
        total.add(accountingLocalizer.localize(PdfLocalizationName.total));
        total.add(getValueAsString(transaction.getTotalDebit(), decimalFormat));
        total.add(getValueAsString(transaction.getTotalCredit(), decimalFormat));
        return total;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat format) {
        if (value == null) {
            return "";
        }
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return format.format(value);
        } else {
            return "(" + format.format(value.abs()) + ")";
        }
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.journalReport);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.JOURNAL_REPORT;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Journal_Report_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
