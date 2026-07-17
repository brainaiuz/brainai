package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol Bahodirov
 * Date: 11.01.13
 * Time: 11:07
 */
public class ManualJournalViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, IPostPDFHandler {

    @Autowired
    ManualEntryServiceLocal manualEntryServiceLocal;

    private String shortDateFormat = "";
    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        RequestObject requestObject = (RequestObject) dataClass;
        NewManualTransaction manualTransaction = manualEntryServiceLocal.getManualJournal(requestObject.getObjectID());
        boolean enabledDepartment = manualTransaction.isEnabledDepartmentRelation();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextSummaryView summaryView = new ITextSummaryView();
        pdfData.setTableName(pdfWfmMessageSource.localize(PdfLocalizationName.manualTransaction));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";

        ITextTableList top = new ITextTableList(4);
        top.setBorderWidth(0);
        top.addTableWidthPercentage(50, 50, 50);

        ITextTableList center = new ITextTableList(enabledDepartment ? 7 : 6);
        center.addTableWidthPercentage(60, 60, 40, 40);

        ITextTableList bottom = new ITextTableList(3);
        bottom.addTableWidthPercentage(70, 90, 90);

        topPanel(top, manualTransaction);
        summaryView.addTable(top);

        centerPanel(center, manualTransaction, priceScaleFormat);
        summaryView.addTable(center);

        bottomPanel(bottom, manualTransaction, priceScaleFormat);
        summaryView.addTable(bottom);

        pdfData.setSummaryView(summaryView);

        return pdfData;
    }

    private void topPanel(ITextTableList top, NewManualTransaction manualTransaction) {
        CellData[] topHeaders = new CellData[4];
        com.lowagie.text.Font defaultFont = new com.lowagie.text.Font(Font.UNDEFINED, 8, com.lowagie.text.Font.BOLD);
        topHeaders[0] = new CellData(commonLocalizer.localize(PdfLocalizationName.narration));
        topHeaders[0].setFont(defaultFont);
        topHeaders[1] = new CellData(commonLocalizer.localize(PdfLocalizationName.date));
        topHeaders[1].setFont(defaultFont);
        topHeaders[2] = new CellData(commonLocalizer.localize(PdfLocalizationName.referenceNumber));
        topHeaders[2].setFont(defaultFont);
        topHeaders[3] = new CellData(commonLocalizer.localize(PdfLocalizationName.number));
        topHeaders[3].setFont(defaultFont);
        setBgColor(topHeaders);
        top.addPdfTableRows(topHeaders);

        CellData[] topRows = new CellData[4];
        topRows[0] = new CellData(manualTransaction.getNarration());
        //topRows[1] = new CellData(dateFormat(manualTransaction.getDate().getNonConvertedDate()));
        topRows[1] = new CellData(ServerUtils.dateFormat(manualTransaction.getDate().getNonConvertedDate(), shortDateFormat));
        topRows[2] = new CellData(manualTransaction.getReference());
        topRows[3] = new CellData(manualTransaction.getNumber());
        setBgColor(topRows);
        top.addPdfTableRows(topRows);
    }

    private void centerPanel(ITextTableList center, NewManualTransaction manualTransaction, DecimalFormat priceScaleFormat) {
        boolean enabledDepartment = manualTransaction.isEnabledDepartmentRelation();
        if(enabledDepartment){
        center.addPdfTableHeader(
                commonLocalizer.localize(PdfLocalizationName.account),
                commonLocalizer.localize(PdfLocalizationName.name),
                commonLocalizer.localize(PdfLocalizationName.description),
                commonLocalizer.localize(PdfLocalizationName.debit),
                commonLocalizer.localize(PdfLocalizationName.credit),
                commonLocalizer.localize(PdfLocalizationName.project),
                commonLocalizer.localize(PdfLocalizationName.department));
        } else {
            center.addPdfTableHeader(
                    commonLocalizer.localize(PdfLocalizationName.account),
                    commonLocalizer.localize(PdfLocalizationName.name),
                    commonLocalizer.localize(PdfLocalizationName.description),
                    commonLocalizer.localize(PdfLocalizationName.debit),
                    commonLocalizer.localize(PdfLocalizationName.credit),
                    commonLocalizer.localize(PdfLocalizationName.project));
        }
        for (NewManualTransactionItem item : manualTransaction.getItems()) {
            CellData[] datas = new CellData[enabledDepartment ? 7 : 6];
            datas[0] = new CellData(item.getAccountItem().getName());
            datas[1] = new CellData(item.getCustomerOrSupplier() != null ? item.getCustomerOrSupplier().getName() : "");
            datas[2] = new CellData(item.getDescription());
            datas[3] = new CellData(item.getDebit() != null ? priceScaleFormat.format(item.getDebit()) : "");
            datas[4] = new CellData(item.getCredit() != null ? priceScaleFormat.format(item.getCredit()) : "");
            datas[5] = new CellData(item.getProject() != null ? item.getProject().getName() : "");
            if(enabledDepartment){
                datas[6] = new CellData(item.getDepartment() != null ? item.getDepartment().getName() : "");
            }
            center.addPdfTableRows(datas);
        }
    }

    private void bottomPanel(ITextTableList bottom, NewManualTransaction manualTransaction, DecimalFormat priceScaleFormat) {
        bottom.setTotalWidth(250);
        bottom.setTableAlignment(Element.ALIGN_RIGHT);
        bottom.addPdfTableHeader("", commonLocalizer.localize(PdfLocalizationName.debit), commonLocalizer.localize(PdfLocalizationName.credit));

        CellData[] currency = new CellData[3];
        currency[0] = new CellData(localize(PdfLocalizationName.total) + "( " + manualTransaction.getCurrency().getName() + " )");
        currency[1] = new CellData(priceScaleFormat.format(manualTransaction.getDebitTotal()));
        currency[2] = new CellData(priceScaleFormat.format(manualTransaction.getCreditTotal()));

        CellData[] baseCurrency = new CellData[3];
        baseCurrency[0] = new CellData(localize(PdfLocalizationName.total) + "( " + manualTransaction.getBaseCurrency().getName() + " )");
        baseCurrency[1] = new CellData(priceScaleFormat.format(manualTransaction.getDebitTotal().divide(manualTransaction.getExchangeRate(), 2, RoundingMode.HALF_UP)));
        baseCurrency[2] = new CellData(priceScaleFormat.format(manualTransaction.getCreditTotal().divide(manualTransaction.getExchangeRate(), 10, RoundingMode.HALF_UP)));

        bottom.addPdfTableRows(currency);
        bottom.addPdfTableRows(baseCurrency);
    }

    private void setBgColor(CellData[] cellDatas) {
        for (CellData items : cellDatas) {
            items.setBgColor(new Color(239, 239, 239));
        }
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdf.setBaseInvoice(baseInvoice);

        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        EdsUser user = uploadManager.getUser();
        pdf.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        NewManualTransaction manualTransaction = manualEntryServiceLocal.getManualJournal(requestObject.getObjectID());
        boolean enabledDepartmentRelation = manualTransaction.isEnabledDepartmentRelation();
        //draw details
        CustomisedITextTable detailsTable = new CustomisedITextTable();
        detailsTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        detailsTable.addRowWithCode(TRANSACTION_NUMBER, "Reference Number", escapeHtml(manualTransaction.getReference()));
        detailsTable.addRowWithCode(NARRATION, "Narration", escapeHtml(manualTransaction.getNarration()));
        detailsTable.addRowWithCode(NUMBER, "Number", escapeHtml(manualTransaction.getNumber()));
        detailsTable.addRowWithCode(ITEM_DATE, "Date", ServerUtils.dateFormat(manualTransaction.getDate().getDate(), "dd, MMM, yyyy"));
        detailsTable.addRowWithCode(ENABLED_DEPARTMENT, "", enabledDepartmentRelation ? "YES" : "NO");
        baseInvoice.setCustomBillToAddress(detailsTable);

        //draw total table

        CustomisedITextTable totalTable = new CustomisedITextTable();
        NumberToWord numberToWordConverter = new NumberToWord_en();
        totalTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        //Foreign Currency
        totalTable.addRowWithCode(ITEM_DEBIT_TOTAL, priceScaleFormat.format(manualTransaction.getDebitTotal()));
        totalTable.addRowWithCode(ITEM_CREDIT_TOTAL, priceScaleFormat.format(manualTransaction.getCreditTotal()));
        totalTable.addRowWithCode(CURRENCY, manualTransaction.getCurrency() != null ? escapeHtml(manualTransaction.getCurrency().getName()) : "");
        //Base Currency
        totalTable.addRowWithCode(ITEM_BASE_DEBIT_TOTAL, priceScaleFormat.format(manualTransaction.getDebitTotal().divide(manualTransaction.getExchangeRate(), 2, RoundingMode.HALF_UP)));
        totalTable.addRowWithCode(ITEM_BASE_CREDIT_TOTAL, priceScaleFormat.format(manualTransaction.getCreditTotal().divide(manualTransaction.getExchangeRate(), 10, RoundingMode.HALF_UP)));
        totalTable.addRowWithCode(BASE_CURRENCY, manualTransaction.getBaseCurrency() != null ? escapeHtml(manualTransaction.getBaseCurrency().getName()) : "");
        if (manualTransaction.getDebitTotal() != null && manualTransaction.getCreditTotal() != null) {
            totalTable.addRowWithCode("TOTAL_WORD_DEBIT", numberToWordConverter.convert(manualTransaction.getDebitTotal().setScale(2, RoundingMode.HALF_UP)));
            totalTable.addRowWithCode("TOTAL_WORD_CREDIT", numberToWordConverter.convert(manualTransaction.getCreditTotal().setScale(2, RoundingMode.HALF_UP)));
        }


        baseInvoice.setCustomTotalTable(totalTable);

        //draw items table
        if (manualTransaction.getItems() != null && manualTransaction.getItems().length > 0) {
            CustomisedITextTable itemTable = new CustomisedITextTable();
            itemTable.addColumnOrder(ACCOUNT_CODE, ACCOUNT_NAME, ITEM_DESCRIPTION, REFERENCE, NAME, DEBIT, CREDIT, RELATED_PROJECT, PARENT_PROJECT, DEPARTMENT);
            itemTable.addHeaderColumns("Account Code", "Name Of Account", "Description", "Reference", "Name", "Debit Amt", "Credit Amt", "Project", "Project Main", "Department");
            for (NewManualTransactionItem item : manualTransaction.getItems()) {
                String accountCode = item.getAccountItem() != null ? escapeHtml(item.getAccountItem().getCode()) : "";
                String accountName = item.getAccountItem() != null ? escapeHtml(item.getAccountItem().getName()) : "";
                String description = escapeHtml(item.getDescription());
                String reference =  escapeHtml(item.getReference());
                String name = item.getCustomerOrSupplier() != null ? escapeHtml(item.getCustomerOrSupplier().getName()) : "";
                String debit = item.getDebit() != null ? priceScaleFormat.format(item.getDebit()) : "";
                String credit = item.getCredit() != null ? priceScaleFormat.format(item.getCredit()) : "";
                String project = item.getProject() != null ? escapeHtml(item.getProject().getName()) : "";
                String parentProject = item.getParentProject() != null ? escapeHtml(item.getParentProject().getName()) : "";
                String department = item.getDepartment() != null ? escapeHtml(item.getDepartment().getName()) : "";
                itemTable.addRow(accountCode, accountName, description, reference, name, debit, credit, project, parentProject, department);
            }
            baseInvoice.setCustomProductTable(itemTable);
        }
        pdf.setCreatorData(getCreatorData(user));
        return pdf;
    }

    private ITextUserData getCreatorData(EdsUser user) {
        ITextUserData result = new ITextUserData();
        if (user != null) {
            result.setFullName(user.getFullName());
            result.setEmail(user.getEmail());
        }
        return result;
    }

    @Override
    protected String getPdfLogoUrl(EdsCompany edsCompany, boolean hasPhantom) throws IOException {
        String url = super.getPdfAccountingLogoUrl(edsCompany);
        if (StringUtils.isNotEmpty(url)) {
            return url;
        }
        return super.getPdfLogoUrl(edsCompany, hasPhantom);
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        TransactionPDFObject requestObject = new TransactionPDFObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (!"".equals(request.getParameter("templateID"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateID")));
        }
        return requestObject;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof TransactionPDFObject) {
            return ((TransactionPDFObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("ManualTransaction_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize(PdfLocalizationName.manualTransaction);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.MANUAL_ENTRY;
    }
}
