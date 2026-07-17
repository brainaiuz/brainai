package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransferMoneyData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Dilshod Madrahimov on 5/2/2016.
 */
public class TransferMoneyViewPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    AccountingService accountingService;
    String shortDateFormat = "MM/dd/yyyy";

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        TransferMoneyData transferMoneyData = accountingService.getBankAccountSummaryData(filterParameter.getObjectId(), null);
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : shortDateFormat;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setTableName("Transfer Money");
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, false));
        ITextSummaryView summaryView = new ITextSummaryView();
        ITextTableList center = new ITextTableList(3);
        summaryView.addTable(center);
        centerPanel(center, transferMoneyData);
        pdfData.setSummaryView(summaryView);

        return pdfData;
    }

    private void centerPanel(ITextTableList center, TransferMoneyData transferMoneyData) {
        Font defaultFont = new Font(Font.UNDEFINED, 8, Font.BOLD);
        CellData[] centerRowTitle;
        CellData[] centerRowValue;

        //first row
        centerRowTitle = new CellData[3];
        centerRowTitle[0] = new CellData("Date");
        centerRowTitle[0].setFont(defaultFont);
        centerRowTitle[1] = new CellData("From");
        centerRowTitle[1].setFont(defaultFont);
        centerRowTitle[2] = new CellData("To");
        centerRowTitle[2].setFont(defaultFont);
        center.addTableWidthPercentage(33, 33, 34);
        center.setBorderWidth(0);
        center.addPdfTableRows(centerRowTitle);

        centerRowValue = new CellData[3];
        centerRowValue[0] = new CellData(ServerUtils.dateFormat(transferMoneyData.getTransferMoneyDate().getNonConvertedDate(), shortDateFormat));
        centerRowValue[1] = new CellData(transferMoneyData.getFromAccount().getName());
        centerRowValue[2] = new CellData(transferMoneyData.getToAccount().getName());
        center.addPdfTableRows(centerRowValue);

        //second row
        centerRowTitle = new CellData[3];
        centerRowTitle[0] = new CellData("Reference");
        centerRowTitle[0].setFont(defaultFont);
        centerRowTitle[1] = new CellData("Amount");
        centerRowTitle[1].setFont(defaultFont);
        centerRowTitle[2] = new CellData("");
        centerRowTitle[2].setFont(defaultFont);

        center.addPdfTableRows(centerRowTitle);

        centerRowValue = new CellData[3];
        centerRowValue[0] = new CellData(transferMoneyData.getReference());
        centerRowValue[1] = new CellData(getMoneyFormat(transferMoneyData.getAmount()));
        centerRowValue[2] = new CellData("");
        center.addPdfTableRows(centerRowValue);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Transfer_Money" + "_" + dateFormat(user.getUserDate()));
    }
}
