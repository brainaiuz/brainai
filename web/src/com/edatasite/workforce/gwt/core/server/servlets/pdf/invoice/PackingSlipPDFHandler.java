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
 * User: Atabek Boboyev
 * Date: 13.07.12
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class PackingSlipPDFHandler extends BaseInvoicePdfHandler {

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
//        pdfData.setTableName(getTableName() != null ? getTableName() :accountingLocalizer.localizeAccounting(PdfLocalizationName.packingSlip));
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        baseInvoice.setPackingSlip(true);
        baseInvoice.setPackingSlipTitle(getTableName(invoice) != null ? getTableName(invoice) : accountingLocalizer.localizeAccounting(PdfLocalizationName.packingSlip));

        Map<String, String> clientData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(clientData);


        if (invoice.getQuoteNumber() != null && !invoice.getQuoteNumber().isEmpty()) {
            String invoiceDate = invoice.getInvoiceDate() != null ? (" - " + getCompanyShortDateFormat(edsUser.getCompany()).format(invoice.getInvoiceDate().getNonConvertedDate())) : "";
            invoice.setInvoiceNumber(invoice.getQuoteNumber() + invoiceDate);
        }

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

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
        columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
        columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty));
        baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns/*productColumnName, addColumnProduct*/));
        baseInvoice.getProductTable().addTableWidthPercentage(0.1f, 0.5f, 1.4f, 0.2f);


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
        // Creator data
        pdfData.setCreatorData(getCreatorData(invoice));
        //Client code
        EdsCrmAccount client = clientManager.get(invoice.getClientID());
        baseInvoice.setClientCode(client.getNumber() != null ? escapeHtml(client.getNumber()) : "");
        // Calculate total date
        baseInvoice.setTotalDay(invoicingSettingsManager.getInvoiceSettings(edsCompany).getPaymentDue());
        // Get Bill Address
        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));

        baseInvoice.setCustomIntroduction(getCustomIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        // Get Number and Dates
        String[] numAndDatesCodes = {RECEIPT_NO,
                                     INV_NUMBER,
                                     QT_NUMBER,
                                     PO_NUMBER,
                                     REFERENCE,
                                     INV_DATE,
                                     INV_DUE_DATE,
                                     PERIOD,
                                     PAYMENT_DATE,
                                     INVOICE_STATUS,
                                     SHIPPING_METHOD,
                                     INVOICE_DUE_TERMS};
        String[] numAndDatesLabels = {pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reciepNumber),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                                      commonLocalizer.localize(PdfLocalizationName.invoiceDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.period),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                                      accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethod),
                                      accountingLocalizer.localizeAccounting(PdfLocalizationName.dueTerms)};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoice, edsUser, numAndDatesCodes, numAndDatesLabels));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoice));
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoice, edsUser, edsCurrency/*, columnCodes, columnLabels*/));
        baseInvoice.setExpenseTableName(commonLocalizer.localizeAccounting(PdfLocalizationName.expense));
        baseInvoice.setCustomExpenseTable(getExpenseCustomTableData(invoice, edsUser));
        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoice));

        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        baseInvoice.setNotes(getInvoiceQuoteNotes(invoice.getHistoryList()));

        //Paypal and Google Chekout Links
        baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoice, edsCurrency, edsCompany.getObjectID(), true));
        baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));
        baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));

        return pdfData;
    }

    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.footerInvoiceText);
    }

    @Override
    public String getFileName() {
        return PS_FILE_NAME;
    }

    @Override
    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsInvoice invoice = invoiceManager.get(objectID);
        map.put(PDF_CLIENT, invoice.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, invoice.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, invoice.getNumber());
        return map;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PACKING_SLIP;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.packingSlip);
    }
}
