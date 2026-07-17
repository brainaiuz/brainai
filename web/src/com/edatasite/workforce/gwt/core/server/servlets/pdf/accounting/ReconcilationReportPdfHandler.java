package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankReconcilationReportData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReconcilationReportPdfHandler extends AbstractITextPostPdfHandler implements PDFConstants, AccountingConstants, IPostPDFHandler {

    @Autowired
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        BankReconcilationReportData reportData = accountingService.getBankAccountReconcilationReport(fp.getStartDateNC(), fp.getEndDateNC(), fp.getObjectId());

        String[] headers = new String[4];
        headers[0] = "Date";
        headers[1] = "Description";
        headers[2] = "Reference";
        headers[3] = "Amount";

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        viewTable.setName("Reconcilation Report");
        viewTable.addColumn(PDFConstants.ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.itemName));
        viewTable.addColumn(PDFConstants.ITEM_DATE, commonLocalizer.localize(PdfLocalizationName.date));
        viewTable.addColumn(PDFConstants.DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        viewTable.addColumn(PDFConstants.REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference));
        viewTable.addColumn(PDFConstants.AMOUNTS, commonLocalizer.localize(PdfLocalizationName.amount));

        List<String> columnsValue = Lists.newArrayList();

        CustomisedITextTable reconcileBalance = new CustomisedITextTable();
        reconcileBalance.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        reconcileBalance.addColumn(AMOUNTS, commonLocalizer.localize(PdfLocalizationName.amount));
        columnsValue.add("Reconciled Balance");
        columnsValue.add(reportData.getReconcileBalance().toString());
        reconcileBalance.addRow(columnsValue.toArray(new String[]{}));

        for (int i = 0; i < reportData.getOutstandingPayments().length; i++) {
            columnsValue.clear();
            columnsValue.add(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.plusOutstandingPayments)));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingPayments()[i].getDate().toString()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingPayments()[i].getDescription()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingPayments()[i].getReference()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingPayments()[i].getAmount().toString()));
            viewTable.addRow(columnsValue.toArray(new String[]{}));
        }

        for (int i = 0; i < reportData.getOutstandingReceipts().length; i++) {
            columnsValue.clear();
            columnsValue.add(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.lessOutstandingReceipts)));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingReceipts()[i].getDate().toString()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingReceipts()[i].getDescription()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingReceipts()[i].getReference()));
            columnsValue.add(StringUtils.defaultString(reportData.getOutstandingReceipts()[i].getAmount().toString()));
            viewTable.addRow(columnsValue.toArray(new String[]{}));
        }
        for (int i = 0; i < reportData.getUnReconciledBankStatementLines().length; i++) {
            columnsValue.clear();
            columnsValue.add(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.plusUnRecBankStatLines)));
            columnsValue.add(StringUtils.defaultString(reportData.getUnReconciledBankStatementLines()[i].getDate().toString()));
            columnsValue.add(StringUtils.defaultString(reportData.getUnReconciledBankStatementLines()[i].getDescription()));
            columnsValue.add(StringUtils.defaultString(reportData.getUnReconciledBankStatementLines()[i].getReference()));
            columnsValue.add(StringUtils.defaultString(reportData.getUnReconciledBankStatementLines()[i].getAmount().toString()));
            viewTable.addRow(columnsValue.toArray(new String[]{}));
        }

        CustomisedITextTable totals = new CustomisedITextTable();
        totals.addColumn(ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        totals.addColumn(AMOUNTS, commonLocalizer.localize(PdfLocalizationName.amount));

        columnsValue.clear();
        columnsValue.add(commonLocalizer.localize(PdfLocalizationName.totalOutstandingPayments));
        columnsValue.add(reportData.getTotalOutstandingPayments().toString());
        totals.addRow(columnsValue.toArray(new String[]{}));

        columnsValue.clear();
        columnsValue.add(commonLocalizer.localize(PdfLocalizationName.totalOutstandingReceipts));
        columnsValue.add(reportData.getTotalOutstandingReceipts().toString());
        totals.addRow(columnsValue.toArray(new String[]{}));

        columnsValue.clear();
        columnsValue.add(commonLocalizer.localize(PdfLocalizationName.totalUnRecStatLines));
        columnsValue.add(reportData.getTotalUnReconciledBankStatementLines().toString());
        totals.addRow(columnsValue.toArray(new String[]{}));

        CustomisedITextTable balance = new CustomisedITextTable();
        balance.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));
        balance.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        balance.addColumn(AMOUNTS, commonLocalizer.localize(PdfLocalizationName.amount));

        columnsValue.clear();
        columnsValue.add(fp.getStartDateNC());
        columnsValue.add(commonLocalizer.localize(PdfLocalizationName.bankAccountBalanceIn) + " kpi.com");
        columnsValue.add(reportData.getBankAccountBalanceInWFT().toString());
        balance.addRow(columnsValue.toArray(new String[]{}));

        CustomisedITextTable balanceAtBank = new CustomisedITextTable();
        balanceAtBank.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));
        balanceAtBank.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        balanceAtBank.addColumn(AMOUNTS, commonLocalizer.localize(PdfLocalizationName.amount));

        columnsValue.clear();
        columnsValue.add(fp.getStartDateNC());
        columnsValue.add("Balance at Bank");
        columnsValue.add(reportData.getBalanceAtBank().toString());
        balanceAtBank.addRow(columnsValue.toArray(new String[]{}));


        customData.put("BALANCE", balance);
        customData.put("RECONCILATE", reconcileBalance);
        customData.put("VIEW_TABLE", viewTable);
        customData.put("TOTAL", totals);
        customData.put("BALANCE_AT_BANK", balanceAtBank);


        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("RECONCILATION_REPORT_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Reconcilation Report";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.RECONCILATION_REPORT;
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
