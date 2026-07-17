package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 18.05.12
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public class CheckPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private BankCheckManager bankCheckManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    public GenericSettingsManager genericSettingsManager;
    private CurrencyItem currencyItem;
    private String number;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        RequestObject requestObject = (RequestObject) dataClass;
        BankCheckData checkData = bankCheckManager.get(requestObject.getObjectID()).createBankCheckData(true);

        EdsUser user = bankCheckManager.getUser();
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
        BankAccountItem bankAccountItem = checkData.getBankAccount();
        NumberToWord numberToWord = new NumberToWord_en();
        boolean isProjectLineItemEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        CustomisedITextTable checkDataTable = new CustomisedITextTable();
        checkDataTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        number = checkData.getNumberData() != null ? escapeHtml(checkData.getNumberData().getNumberString()) : "";
        String amountWord = "";
        if (checkData.getAmount() != null) {
            amountWord = numberToWord.convert(checkData.getAmount());
        }
        checkDataTable.addRowWithCode(BANK_NAME, commonLocalizer.localize("bankName", "Bank Name"), escapeHtml(bankAccountItem.getName()));
        checkDataTable.addRowWithCode(ACCOUNT_BALANCE, accountingLocalizer.localize("balanceAmount", "Balance Amount"), priceScaleFormat.format(bankAccountItem.getBalance()));
        checkDataTable.addRowWithCode(ITEM_DESCRIPTION, commonLocalizer.localize("description", "Description"), escapeHtml(bankAccountItem.getDescription()));
        checkDataTable.addRowWithCode(BankCheckData.NUMBER, commonLocalizer.localize(PdfLocalizationName.number), number);
        checkDataTable.addRowWithCode(BankCheckData.PAY_TO, commonLocalizer.localize(PdfLocalizationName.payTo), escapeHtml(checkData.getPayTo()));
        checkDataTable.addRowWithCode(BankCheckData.DATE, commonLocalizer.localize(PdfLocalizationName.date), dateFormat(checkData.getDate().getNonConvertedDate()));
        checkDataTable.addRowWithCode(BankCheckData.AMOUNT, commonLocalizer.localize(PdfLocalizationName.amount), priceScaleFormat.format(checkData.getAmount()));
        checkDataTable.addRowWithCode(BankCheckData.AMOUNT_STRING_WORD, commonLocalizer.localize("amountInWords"), escapeHtml(amountWord));
        checkDataTable.addRowWithCode(BankCheckData.ADDRESS, commonLocalizer.localize(PdfLocalizationName.address), escapeHtml(checkData.getAddress()));
        checkDataTable.addRowWithCode(BankCheckData.MEMO, commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(checkData.getMemo()));
        if (!isProjectLineItemEnable) {
            checkDataTable.addRowWithCode(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.project), checkData.getProject() != null ? escapeHtml(checkData.getProject().getName()) : "");
            checkDataTable.addRowWithCode(PROJECT_NUMBER, commonLocalizer.localize("projectNumber", "Project Number"), checkData.getProject() != null ? escapeHtml(checkData.getProject().getNumber()) : "");
        }

        /*item table*/
        CustomisedITextTable itemsTable = new CustomisedITextTable();
        itemsTable.addColumn(ITEM_ACCOUNT, commonLocalizer.localize(PdfLocalizationName.account));
        itemsTable.addColumn(ITEM_AMOUNT, commonLocalizer.localize(PdfLocalizationName.amount));
        itemsTable.addColumn(ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        itemsTable.addColumn(ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        itemsTable.addColumn(BILL_TO_HEADER, pdfWfmMessageSource.localize("billTo", "Bill To").replace(":", ""));
        if (isProjectLineItemEnable) {
            itemsTable.addColumn(ITEM_PROJECT, commonLocalizer.localize(PdfLocalizationName.project));
        }
        List<String> values = Lists.newArrayList();
        for (BankCheckItem item : checkData.getItems()) {
            values.add(item.getAccount() != null ? escapeHtml(item.getAccount().getName()) : "");
            values.add(priceScaleFormat.format(item.getAmount()));
            values.add(escapeHtml(item.getDescription()));
            values.add(item.getCrmAccount() != null ? escapeHtml(item.getCrmAccount().getName()) : "");
            values.add(item.getClient() != null ? escapeHtml(item.getClient().getName()) : "");
            if (isProjectLineItemEnable) {
                values.add(item.getProject() != null ? escapeHtml(item.getProject().getName()) : "");
            }
            itemsTable.addRow(values.toArray(new String[]{}));
            values.clear();
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("CHECK_HEADER_DATA", checkDataTable);
        customData.put("ITEMS_TABLE", itemsTable);

        pdfData.setCustomData(customData);
        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        TransactionPDFObject requestObject = new TransactionPDFObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (request.getParameter("templateID") != null && !"".equals(request.getParameter("templateID"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateID")));
        }
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Check_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.BANK_CHECK;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.check) + " #" + number;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof TransactionPDFObject) {
            return ((TransactionPDFObject) object).getTemplateID();
        }
        return null;
    }
}
