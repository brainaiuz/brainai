package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 13:37:02
 * To change this template use File | Settings | File Templates.
 */
public class CreditNoteViewPDFHandler extends BaseInvoicePdfHandler {

    public String getFileName() {
        return PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.name().equals(getPdfCodeName(null)) ? DN_FILE_NAME : CN_FILE_NAME;
    }

    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsInvoice creditNote = invoiceManager.get(objectID);
        map.put(PDF_CLIENT, creditNote.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, creditNote.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, creditNote.getNumber());
        return map;
    }

    @Override
    protected String getFromInvoice() {
        return SALE_INVOICE;
    }

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        boolean isDebitNote = PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.equals(getPdfCodeName(null));
        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(isDebitNote ? PdfLocalizationName.debitNote : PdfLocalizationName.creditNote));
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        boolean isAddDiscountColumn = addDiscountColumn(invoice);

        Map<String, String> clientSupplierData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(clientSupplierData);
        baseInvoice.setIntroduction(getIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(INV_NUMBER, accountingLocalizer.localizeAccounting(isDebitNote ? PdfLocalizationName.debitNoteNumber : PdfLocalizationName.creditNoteNumber));
        numDateTableRowKeys.put(INV_DATE, accountingLocalizer.localizeAccounting(isDebitNote ? PdfLocalizationName.debitNoteDate : PdfLocalizationName.creditNoteDate));
        numDateTableRowKeys.put(INV_DUE_DATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.dueDate));

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys/*numDatesColumns, addRowNumDates*/));
        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));

        String curSymbol = getCurrencySymbol(edsCurrency, false);
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put(ITEM_NO, commonLocalizer.localizeAccounting(PdfLocalizationName.no));
        columns.put(ITEM_NAME, accountingLocalizer.localizeAccounting(PdfLocalizationName.productOrService));
        columns.put(ITEM_DESCRIPTION, commonLocalizer.localizeAccounting(PdfLocalizationName.description));
        columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qtyOrHrs));
        columns.put(ITEM_UNIT_PRICE, " " + accountingLocalizer.localizeAccounting(PdfLocalizationName.unitPrice) + " " + curSymbol);
        if (isAddDiscountColumn) {
            columns.put(ITEM_DISCOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.discount));
        }
        columns.put(ITEM_NET_AMOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.netAmount) + " " + curSymbol);
        columns.put(ITEM_TOTAL_AMOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalAmount) + " " + curSymbol);
        baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns/*productColumnName, addColumnProduct*/));
        baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2.5f, 1f, 1f, 1f, 1f, 1f);
        baseInvoice.setAccount(getAccountTable(edsUser, invoice, getSupplier(clientOrSupplier)));

        if (invoice.getPaymentItems() == null && invoice.getID() != null) {
            invoice.setPaymentItems(EdsInvoice.getPaymentItemsList(invoiceManager.get(invoice.getID())).toArray(new PaymentItem[]{}));
        }
        invoice.setCreditNote(true);

        LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();
        rowsMap.put(SUBTOTAL, accountingLocalizer.localizeAccounting(PdfLocalizationName.subtotal));
        if (isAddDiscountColumn) {
            rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
        }
        rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
        baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));

        return pdfData;
    }

    @Override
    protected <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoice,
                                                                                                   EdsUser edsUser,
                                                                                                   EdsCurrency edsCurrency,
                                                                                                   ClientOrSupplier clientOrSupplier,
                                                                                                   EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();

        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));

        baseInvoice.setCustomIntroduction(getCustomIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        boolean isDebitNote = PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.equals(getPdfCodeName(null));

        String[] numAndDateCodes = {INV_NUMBER,
                                    INV_DATE,
                                    INV_DUE_DATE,
                                    PO_NUMBER,
                                    REFERENCE,
                RELATED_INVOICE_NUMBER,
                RELATED_INVOICE_DATE,
                QRCODE};
        String[] numAndDateLabels = {accountingLocalizer.localizeAccounting(isDebitNote ? PdfLocalizationName.debitNoteNumber : PdfLocalizationName.creditNoteNumber),
                                     accountingLocalizer.localizeAccounting(isDebitNote ? PdfLocalizationName.debitNoteDate : PdfLocalizationName.creditNoteDate),
                                     accountingLocalizer.localizeAccounting(PdfLocalizationName.dueDate),
                                     accountingLocalizer.localizeAccounting(PdfLocalizationName.poNumber),
                                     accountingLocalizer.localizeAccounting(PdfLocalizationName.reference),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.creditNoteNumber),
                commonLocalizer.localize(PdfLocalizationName.invoiceDate),
                "QR Code"};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoice, edsUser, numAndDateCodes, numAndDateLabels));

        //Company Data
        EdsCompany edsCompany = edsUser.getCompany();
        // Set Currency
        baseInvoice.setCurrency(getCurrencySymbol(edsCurrency, true));
        // Set Currency Name
        baseInvoice.setCurrencyName(edsCurrency.getName() != null ? edsCurrency.getName() : "");
        //Client code
        EdsCrmAccount client = getClientOrSupplier(invoice.getClientID());
        baseInvoice.setClientCode(client.getNumber() != null ? client.getNumber() : "");
        // Calculate total date
        baseInvoice.setTotalDay(invoicingSettingsManager.getInvoiceSettings(edsCompany).getPaymentDue());

        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoice, edsUser, edsCurrency/*, codes, labels*/));
        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoice));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        return pdfData;
    }

    private boolean addDiscountColumn(NewInvoice newInvoice) {
        if (newInvoice == null || newInvoice.getItems() == null) {
            return false;
        } else {
            newInvoice.getItems();
        }
        for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
            if ((invoiceItem.getDiscountAmount() != null && invoiceItem.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) ||
                (invoiceItem.getDiscountPercent() != null && invoiceItem.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getFooterContactText() {
        return PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.equals(getPdfCodeName(null)) ?
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.debitNoteFooterContactText) :
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.creditNoteFooterContactText);
    }

    protected EdsCrmAccount getClientOrSupplier(Integer clientSupplierId) {
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.equals(getPdfCodeName(null))) {
            return accountingLocalizer.localizeAccounting(PdfLocalizationName.debitNote);
        } else {
            return accountingLocalizer.localizeAccounting(PdfLocalizationName.creditNote);
        }
    }
}
