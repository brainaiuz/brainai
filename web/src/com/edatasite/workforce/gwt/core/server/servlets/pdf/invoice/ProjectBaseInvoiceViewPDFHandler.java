package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 31-May-2009
 * Time: 08:08:22
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBaseInvoiceViewPDFHandler extends BaseInvoicePdfHandler {

    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsInvoice invoice = invoiceManager.get(objectID);
        map.put(PDF_CLIENT, invoice.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, invoice.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, invoice.getNumber());
        return map;
    }

    protected String getFromInvoice() {
        return SALE_INVOICE;
    }

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesInvoice));
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();

        Map<String, String> clientData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(clientData);

        baseInvoice.setIntroduction(getIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(INV_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo));
        numDateTableRowKeys.put(QT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber));
        numDateTableRowKeys.put(PO_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber));
        numDateTableRowKeys.put(REFERENCE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        numDateTableRowKeys.put(COMP_VAT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxNumber));
        numDateTableRowKeys.put(INV_DATE, commonLocalizer.localize(PdfLocalizationName.invoiceDate));
        numDateTableRowKeys.put(INV_DUE_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate));
        numDateTableRowKeys.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));


        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.isShowVatNumberInInvoices() && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.VAT_RETURN_ENABLE)) {
            numDateTableRowKeys.put(COMP_VAT_NUMBER, fs.getTaxIdDisplayNumber() != null ? fs.getTaxIdDisplayNumber() : "");
        }

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys/*numDatesColumns, addRowNumDates*/));
        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));
        baseInvoice.setExpenseTableName(commonLocalizer.localizeAccounting(PdfLocalizationName.expense));

        String curSymbol = getCurrencySymbol(edsCurrency, false);

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
        columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
        columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE)) {
            columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.hours));
        }
        columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + " " + curSymbol);
        columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
        columns.put(ITEM_NET_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount) + " " + curSymbol);
        columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + " " + curSymbol);
        baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns/*productColumnName, addColumnProduct*/));
        baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 2.5f, 1f, 1f, 1f, 1f, 1f);

        LinkedHashMap<String, String> columnsExpense = new LinkedHashMap<>();
        columnsExpense.put(ITEM_EXPENSE_CATEGORY, commonLocalizer.localizeAccounting(PdfLocalizationName.category));
        columnsExpense.put(ITEM_EXPENSE_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        columnsExpense.put(ITEM_EXPENSE_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount));

        baseInvoice.setExpenseTable(getExpenseTableData(invoice, edsUser, edsCurrency, columnsExpense/*productColumnName, addColumnProduct*/));
        baseInvoice.getExpenseTable().addTableWidthPercentage(0.25f, 0.5f, 0.25f);

        LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();
        rowsMap.put(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal));
        rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
        rowsMap.put(SHIPPING_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipping));
        if (invoice.getBillableExpenseAmount() != null && invoice.getBillableExpenseAmount().compareTo(ZERO) != 0) {
            rowsMap.put(BILL_EXP_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billableExpenseTotal));
        }
        rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
        baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));

        baseInvoice.setTermsConditions(getTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setBank(getBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setAccount(getAccountTable(edsUser, invoice, getSupplier(clientOrSupplier)));


        baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoice, edsCurrency, edsUser.getCompany().getObjectID(), false));
        baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoice, edsUser.getCompany().getObjectID(), false, true));
        baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoice, edsUser.getCompany().getObjectID(), false, true));
        return pdfData;
    }

    @Override
    protected <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        ITextUserData userData = new ITextUserData();
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setUserData(userData);
        EdsCompany edsCompany = edsUser.getCompany();
        // Company Data
        pdfData.setCompanyData(getCompanyData(edsCompany, true, false));
        // User Data
        userData.setFullName(edsUser.getFullName());
        if (edsUser.isEmployee()) {
            EdsEmployee emp = getEmployeeManager().get(edsUser.getObjectID());
            userData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
            userData.setEmail(edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? escapeHtml(edsUser.getEmail()) : "");
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }
        // Set Currency
        baseInvoice.setCurrency(getCurrencySymbol(edsCurrency, true));
        // Set Currency Name
        baseInvoice.setCurrencyName(getCurrencyName(edsCurrency));
        //Exchange Rate
        baseInvoice.setExchangeRate(getExchangeRate(invoice));
        //Client code
        EdsCrmAccount client = clientManager.get(invoice.getClientID());
        baseInvoice.setClientCode(client.getNumber() != null ? escapeHtml(client.getNumber()) : "");
        // Calculate total date
        baseInvoice.setTotalDay(invoicingSettingsManager.getInvoiceSettings(edsCompany).getPaymentDue());
        // Get Bill Address
        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));

        baseInvoice.setCustomIntroduction(getCustomIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        // Get Number and Dates
        String[] numAndDatesCodes = {RECEIPT_NO, INV_NUMBER, QT_NUMBER, PO_NUMBER, REFERENCE, INV_DATE, INV_DUE_DATE, PERIOD, INVOICE_STATUS, /*COMP_VAT_NUMBER,*/ PAYMENT_DATE, SHIPPING_METHOD, PERIOD_START_DATE, PERIOD_END_DATE};
        String[] numAndDatesLabels = {pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reciepNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxNumber),
                commonLocalizer.localize(PdfLocalizationName.invoiceDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.period),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentDate),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethod),
                "Period Start",
                "Period End"};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoice, edsUser, numAndDatesCodes, numAndDatesLabels));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoice));
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoice, edsUser, edsCurrency/*, columnCodes, columnLabels*/));
        baseInvoice.setExpenseTableName(commonLocalizer.localizeAccounting(PdfLocalizationName.expense));
        baseInvoice.setCustomExpenseTable(getExpenseCustomTableData(invoice, edsUser));
        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoice));
        baseInvoice.setCustomDueAmountTable(getDueAmountTable(invoice, edsUser));
        baseInvoice.setCustomPrepaymentTable(getPrepaymentTable(invoice, edsUser));

        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        baseInvoice.setNotes(getInvoiceQuoteNotes(invoice.getHistoryList()));

        //Paypal and Google Chekout Links
        baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoice, edsCurrency, edsCompany.getObjectID(), true));
        baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));
        baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));
        baseInvoice.setPaymentHistoryTable(getCustomPaymentHistory(invoice, edsUser, edsCurrency));

        return pdfData;
    }

    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceFooterContactText);
    }

    public String getFileName() {
        return PBI_FILE_NAME;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PROJECT_BASED_INVOICE;
    }
}
