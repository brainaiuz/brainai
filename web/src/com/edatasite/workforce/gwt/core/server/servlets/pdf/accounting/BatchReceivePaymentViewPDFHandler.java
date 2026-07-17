package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sherzod on 7/7/2015.
 */
public class BatchReceivePaymentViewPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants, PDFConstants, IPostPDFHandler {

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    protected CommonService commonService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    public GenericSettingsManager genericSettingsManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        boolean isReceivable = Constants.RECEIVABLE.equals(requestObject.getViewName());

        EdsCompany edsCompany = userManager.getUser().getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsCompany);
        DecimalFormat defaultScaleFormat = getPriceScaleNumberFormat(edsCompany, requestObject.getTemplateID());
        DecimalFormat qtyNumberFormat = getQtyNumberFormat(edsCompany, null);

        ReceivePaymentData batchPaymentData = invoiceServiceLocal.getBatchPaymentPdfData(requestObject.getObjectID());

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdf.setBaseInvoice(baseInvoice);

        //draw details table
        CustomisedITextTable detailsTable = new CustomisedITextTable();
        detailsTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        detailsTable.addRowWithCode(TITLE, "Title", (isReceivable ? "Payment Receipt" : "Pay Bill"));
        detailsTable.addRowWithCode("NUMBER", (isReceivable ? pdfWfmMessageSource.localize(PdfLocalizationName.receiptNo) : commonLocalizer.localize(PdfLocalizationName.number)), escapeHtml(batchPaymentData.getNumber()));
        detailsTable.addRowWithCode("DATE", "Date", shortDateFormat.format(batchPaymentData.getDate().getNonConvertedDate()));
        detailsTable.addRowWithCode("CRM_ACCOUNT", isReceivable ? "Received From" : "Paid To", batchPaymentData.getCrmAccount() != null ? escapeHtml(batchPaymentData.getCrmAccount().getName()) : "");
        detailsTable.addRowWithCode(ACCOUNT, commonLocalizer.localize(PdfLocalizationName.bankAccountName), batchPaymentData.getAccount() != null ? escapeHtml(batchPaymentData.getAccount().getName()) : "");
        detailsTable.addRowWithCode(PO_PAYMENT_TYPE, "Payment Type", batchPaymentData.getPaymentMethod() != null ? escapeHtml(batchPaymentData.getPaymentMethod().getName()) : "");
        detailsTable.addRowWithCode(CURRENCY, "Currency", batchPaymentData.getCurrency() != null ? escapeHtml(batchPaymentData.getCurrency().getName()) : "");
        detailsTable.addRowWithCode(BASE_CURRENCY, "Base Currency", batchPaymentData.getBaseCurrency() != null ? escapeHtml(batchPaymentData.getBaseCurrency().getName()) : "");
        detailsTable.addRowWithCode(REFERENCE,  commonLocalizer.localize("reference"),  escapeHtml(batchPaymentData.getReference()));
        detailsTable.addRowWithCode("DEPARTMENT", "Department", batchPaymentData.getDepartment() != null ? escapeHtml(batchPaymentData.getDepartment().getName()) : "");
        detailsTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize("description"), escapeHtml(batchPaymentData.getDescription()));
        detailsTable.addRowWithCode(BANK_ACCOUNT_CODE, "Bank Account Code", batchPaymentData.getAccount() != null ? escapeHtml(batchPaymentData.getAccount().getDescription()) : "");
        detailsTable.addRowWithCode(BANK_ACCOUNT_NUMBER, "Bank Account Number", batchPaymentData.getAccountNumber() != null ? escapeHtml(batchPaymentData.getAccountNumber()) : "");
        detailsTable.addRowWithCode(PROJECT_NUMBER, "Project Number", batchPaymentData.getProject() != null ? escapeHtml(batchPaymentData.getProject().getDescription()) : "");
        detailsTable.addRowWithCode(PROJECT_NAME, "Project Name", batchPaymentData.getProject() != null ? escapeHtml(batchPaymentData.getProject().getName()) : "");
        if (batchPaymentData.getCrmAccountAddress() != null) {
            Address address = batchPaymentData.getCrmAccountAddress();
            detailsTable.addRowWithCode(BILL_ADDRESS_NAME, "Bill To", escapeHtml(address.getName()));
            detailsTable.addRowWithCode(BILL_ADDRESS, "Address Line", escapeHtml(address.getAddress()));
            detailsTable.addRowWithCode(BILL_ADDRESS2, "Address Line 2", escapeHtml(address.getAddressb()));
            detailsTable.addRowWithCode(BILL_CITY, "City", escapeHtml(address.getCity()));
            detailsTable.addRowWithCode(BILL_STATE, "State", escapeHtml(address.getState()));
            detailsTable.addRowWithCode(BILL_COUNTRY, "Country", escapeHtml(address.getCountry()));
            detailsTable.addRowWithCode(BILL_ZIPCODE, "Zipcode", escapeHtml(address.getZipCode()));
        }
        EdsCrmAccount client = batchPaymentData.getCrmAccount() != null ? clientManager.get(batchPaymentData.getCrmAccount().getId()) : null;
        String clientRegistrationNumber = client != null ? escapeHtml(client.getRegistrationNumber()) : "";
        String clientCode = client != null ? escapeHtml(client.getNumber()) : "";
        detailsTable.addRowWithCode("CLIENT_REGISTRATION_NUMBER", "CLIENT_REGISTRATION_NUMBER", clientRegistrationNumber);
        detailsTable.addRowWithCode("CLIENT_CODE", "CLIENT_CODE", clientCode);
        detailsTable.addRowWithCode("PAYMENT_TARGET", "PAYMENT_TARGET", escapeHtml(batchPaymentData.getPaymentTarget()));

        //draw item table
        BigDecimal subTotal = BigDecimal.ZERO;
        StringBuilder invoiceNumbers = new StringBuilder();
        CustomisedITextTable itemTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new LinkedHashMap<>();
        if (batchPaymentData.getPayments() != null && batchPaymentData.getPayments().length > 0) {
            baseInvoice.setProductTableName(isReceivable ? "Payment Receipt" : "Pay Bill");
            itemTable.addColumnOrder(
                    ACCOUNT_CODE,
                    ACCOUNT_NAME,
                    ITEM_AMOUNT,
                    ITEM_PAYMENT_BASE_AMOUNT,
                    INV_NUMBER,
                    INV_DATE,
                    INV_DUE_DATE,
                    ITEM_TOTAL_AMOUNT,
                    PROJECT_NUMBER,
                    PROJECT_NAME,
                    ITEM_NET_AMOUNT,
                    ITEM_SUB_PROJECT,
                    PARENT_PROJECT,
                    PO_NUMBER,
                    ITEM_DISCOUNT_AMOUNT,
                    DUE_AMOUNT,
                    CURRENCY,
                    ITEM_NAME,
                    ITEM_BASE_AMOUNT,
                    ITEM_PAYMENT_REFERENCE,
                    "INVOICE_PRODUCT_NAME",
                    "INVOICE_ITEM_QTY",
                    "INVOICE_ITEM_UNIT_PRICE",
                    "INVOICE_ITEM_NET_AMOUNT",
                    "INVOICE_ITEM_TOTAL_AMOUNT",
                    "INVOICE_ITEM_TAX_AMOUNT",
                    "INVOICE_ITEM_TAX_RATE",
                    "ITEM_TAX_OF_PAYMENT_AMOUNT",
                    "ITEM_TOTAL_PAYMENT_AMOUNT",
                    "NO");

            itemTable.addHeaderColumns(
                    "Account Code",
                    "Head Of Accounts Debited",
                    "Original Amount",
                    "Base Amount",
                    "Invoice Number",
                    "Invoice Date",
                    "Invoice Due Date",
                    "Invoice Total",
                    "Project Number",
                    "Project Name",
                    "Net Amount",
                    "Sub Project",
                    "Main Project",
                    "PO Number",
                    "Discount",
                    "Due Amount",
                    "Currency",
                    "Item Name and Quantity",
                    "Base amount",
                    "Reference",
                    "Invoice Line Item Name",
                    "Invoice Line Item QTY",
                    "Invoice Line Item Unit Price",
                    "Invoice Line Item Net Amount",
                    "Invoice Line Item Total Amount",
                    "Invoice Line Item Tax Amount",
                    "Invoice Line Item Tax Rate",
                    "Invoic Line Item Tax Amount of Payment Amount ",
                    "Invoic Line Item Total Payment Amount ",
                    "No");

            int count = 0;
            for (PaymentData item : batchPaymentData.getPayments()) {
                count = count + 1;
                StringBuilder projectName = new StringBuilder();
                StringBuilder parentProjectName = new StringBuilder();
                StringBuilder itemNameAndQty = new StringBuilder();
                if (batchPaymentData.isEnabledLineItemProject() && item.getLineItemProject() != null && !item.getLineItemProject().isEmpty()) {
                    for (String itemProject : item.getLineItemProject()) {
                        if ("".equals(projectName.toString())) {
                            projectName.append(itemProject);
                        } else {
                            projectName.append(", ").append(itemProject);
                        }
                    }
                    if (item.getLineItemParentProject() != null && !item.getLineItemParentProject().isEmpty()) {
                        for (String parIItem : item.getLineItemParentProject()) {
                            if ("".equals(parentProjectName.toString())) {
                                parentProjectName.append(parIItem);
                            } else {
                                parentProjectName.append(", ").append(parIItem);
                            }
                        }
                    }
                }
                SelectItem currency = item.getCurrency();

                BigDecimal taxPercent = (item.getTaxItem() != null && item.getTaxItem().getEffectiveTaxPercent() != null) ? item.getTaxItem().getEffectiveTaxPercent() : ZERO;
                BigDecimal taxAmountOfPaymentAmount = BigDecimal.ZERO;
                BigDecimal totalPaymentAmount = item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO;
                if (item.getTaxCalculationType() != null && item.getPaymentAmount() != null) {
                    if (TAX_CALCULATION_INCLUSIVE.equals(item.getTaxCalculationType())) {
                        taxAmountOfPaymentAmount = item.getPaymentAmount().multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, BigDecimal.ROUND_HALF_UP);
                    } else if (TAX_CALCULATION_EXCLUSIVE.equals(item.getTaxCalculationType())) {
                        taxAmountOfPaymentAmount = item.getPaymentAmount().multiply(taxPercent).divide(HUNDRED, 4, BigDecimal.ROUND_HALF_UP);
                        totalPaymentAmount = totalPaymentAmount.add(taxAmountOfPaymentAmount);
                    }
                }

                for (Map.Entry<String, BigDecimal> entry : item.getLineItemNameAndQty().entrySet()) {
                    itemNameAndQty.append(entry.getKey()).append(", ").append(qtyNumberFormat.format(entry.getValue())).append("\n");
                }
                itemTable.addRow(
                        item.getAccountItem() != null ? escapeHtml(item.getAccountItem().getDescription()) : "",
                        item.getAccountItem() != null ? escapeHtml(item.getAccountItem().getName()) : "",
                        defaultScaleFormat.format(item.getPaymentAmount() == null ? BigDecimal.ZERO : item.getPaymentAmount()),
                        defaultScaleFormat.format(item.getBasePaymentAmount() == null ? BigDecimal.ZERO : item.getBasePaymentAmount()),
                        escapeHtml(item.getInvoiceNumber()),
                        item.getInvoiceDate() == null ? "" : shortDateFormat.format(item.getInvoiceDate().getDate()),
                        item.getInvoiceDueDate() == null ? "" : shortDateFormat.format(item.getInvoiceDueDate().getDate()),
                        defaultScaleFormat.format(item.getPaymentAmount() == null ? BigDecimal.ZERO : item.getPaymentAmount()),
                        escapeHtml(item.getInvoiceProjectNumber()),
                        escapeHtml(item.getInvoiceProjectName()),
                        item.getTotal() != null ? defaultScaleFormat.format(item.getTotal()) : "",
                        escapeHtml(projectName.toString()),
                        escapeHtml(parentProjectName.toString()),
                        escapeHtml(item.getPoNumber()),
                        item.getTotalDiscount() != null ? defaultScaleFormat.format(item.getTotalDiscount()) : "",
                        item.getTotalDueAmount() != null ? defaultScaleFormat.format(item.getTotalDueAmount()) : "",
                        currency != null ? escapeHtml(currency.getName()) : "",
                        escapeHtml(itemNameAndQty.toString()),
                        item.getBaseTotal() != null ? defaultScaleFormat.format(item.getBaseTotal()) : "",
                        item.getReferenceNumber() != null ? item.getReferenceNumber() : "",
                        escapeHtml(item.getItemName()),
                        defaultScaleFormat.format(item.getItemQty() != null ? item.getItemQty() : BigDecimal.ZERO),
                        defaultScaleFormat.format(item.getItemUnitPrice() != null ? item.getItemUnitPrice() : BigDecimal.ZERO),
                        defaultScaleFormat.format(item.getItemNetAmount() != null ? item.getItemNetAmount() : BigDecimal.ZERO),
                        defaultScaleFormat.format(item.getItemTotalAmount() != null ? item.getItemTotalAmount() : BigDecimal.ZERO),
                        defaultScaleFormat.format(item.getItemTaxAmount() != null ? item.getItemTaxAmount() : BigDecimal.ZERO),
                        defaultScaleFormat.format(taxPercent),
                        defaultScaleFormat.format(taxAmountOfPaymentAmount),
                        defaultScaleFormat.format(totalPaymentAmount),
                        count + "."
                );

                subTotal = subTotal.add(item.getPaymentAmount() != null ? item.getPaymentAmount() : BigDecimal.ZERO);
                invoiceNumbers.append(item.getInvoiceNumber() + " ,");

                if (item.getItemId() != null) {
                    EdsItem edsItem = itemManager.get(item.getItemId());

                    LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                    if (edsItem != null) {
                        //invoice product custom fields
                        if (edsItem.getCustomFields() != null) {
                            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView));
                            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                                if (customFieldItem != null) {
                                    Map<String, String> cols = new HashMap<>();
                                    cols.put(COLUMN_NAME, customFieldItem.getFieldName() != null ? escapeHtml(customFieldItem.getFieldName()) : null);
                                    if (CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                                        cols.put(COLUMN_VALUE, customFieldItem.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                                    } else {
                                        cols.put(COLUMN_VALUE, customFieldItem.getFieldStringValue() != null ? escapeHtml(customFieldItem.getFieldStringValue()) : null);
                                    }
                                    if (customFieldItem.getFieldName() != null) {
                                        itemCusFields.put(escapeHtml(customFieldItem.getFieldName()), cols);
                                    }
                                }
                            }
                        }
                        customFields.put(count + ".", itemCusFields);
                    }
                }
            }
        }

        NumberToWord numberToWordConverter;
        EdsUser user = userManager.getUser();
        if (user.getCompany().getLocale() != null && "ru".equals(user.getCompany().getLocale())) {
            numberToWordConverter = new NumberToWord_ru();
        } else if (user.getCompany().getLocale() != null && "ar".equals(user.getCompany().getLocale())) {
            numberToWordConverter = new NumberToWord_ar();
        } else if (user.getCompany().getLocale() != null && "uz".equals(user.getCompany().getLocale())) {
            numberToWordConverter = new NumberToWord_uz();
        } else {
            numberToWordConverter = new NumberToWord_en();
        }
        BigDecimal dueAmount = batchPaymentData.getTotalAmount() != null ? batchPaymentData.getTotalAmount().subtract(subTotal) : BigDecimal.ZERO;
        detailsTable.addRowWithCode(TOTAL, "Total", defaultScaleFormat.format(subTotal));
        detailsTable.addRowWithCode(DUE_AMOUNT, "Due Amount", defaultScaleFormat.format(dueAmount));
        detailsTable.addRowWithCode(TOTAL_WORD, commonLocalizer.localize(PdfLocalizationName.amountInWords), escapeHtml(numberToWordConverter.toWord(subTotal)));
        detailsTable.addRowWithCode(TOTAL_WORD_ALL, commonLocalizer.localize(PdfLocalizationName.amountInWords), WordUtils.capitalizeFully(escapeHtml(numberToWordConverter.convert(batchPaymentData.getTotalAmount() != null ? batchPaymentData.getTotalAmount() : BigDecimal.ZERO))));
        detailsTable.addRowWithCode(PDFConstants.TOTAL_AMOUNT, commonLocalizer.localize(PdfLocalizationName.total), defaultScaleFormat.format(batchPaymentData.getTotalAmount() != null ? batchPaymentData.getTotalAmount() : BigDecimal.ZERO));
        detailsTable.addRowWithCode("INVOICE_NUMBERS", "Invoice Numbers", !"".equals(invoiceNumbers.toString()) ? escapeHtml(invoiceNumbers.substring(0, invoiceNumbers.toString().length() - 1)) : "");
        numberToWordConverter = new NumberToWord_ar();
        detailsTable.addRowWithCode(TOTAL_ARABIC_WORD_ALL, "Amount in Words", escapeHtml(numberToWordConverter.convert(batchPaymentData.getTotalAmount() != null ? batchPaymentData.getTotalAmount() : BigDecimal.ZERO)));
        detailsTable.addRowWithCode(ACCOUNT_CODE, "Customer Code", batchPaymentData.getCrmAccount() != null && batchPaymentData.getCrmAccount().getId() != null ? escapeHtml(batchPaymentData.getCrmAccount().getId().toString()) : "");
        itemTable.setCustomFields(getCustomFields(batchPaymentData.getCustomFieldItems(), customFields));
        baseInvoice.setCustomProductTable(itemTable);

        baseInvoice.setCustomBillToAddress(detailsTable);
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(batchPaymentData.getBankAccount()));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(batchPaymentData.getBankAccount()));
        pdf.setCreatorData(getCreatorData(uploadManager.getUser()));

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("CURRENT_DATA", getCurrentData());
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ClIENT_ZALOG)) {
            customData.put("INVOICE_ITEMS_DATA", getInvoiceItemsData(requestObject, edsCompany));
        }
        pdf.setCustomData(customData);

        Map<String, String> localizeLabels = new LinkedHashMap<>();
        localizeLabels.put("CUSTOMER_LABEL", commonLocalizer.localize(PdfLocalizationName.customer));
        localizeLabels.put("SUPPLIER_LABEL", commonLocalizer.localize(PdfLocalizationName.supplier));
        localizeLabels.put("BANK_ACCOUNT_LABEL", commonLocalizer.localize(PdfLocalizationName.bankAccount));
        localizeLabels.put("RECEIPT_DATE_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.receiptDate));
        localizeLabels.put("PAID_DATE_LABEL", commonLocalizer.localize(PdfLocalizationName.datePaid));

        pdf.setLocalizeLabels(localizeLabels);
        return pdf;
    }

    private CustomisedITextTable getInvoiceItemsData(TransactionPDFObject requestObject, EdsCompany edsCompany) {
        CustomisedITextTable invoiceItemsData = new CustomisedITextTable();
        DecimalFormat defaultScaleFormat = getPriceScaleNumberFormat(edsCompany, null);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsCompany);
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new LinkedHashMap<>();
        StringBuilder itemNames = new StringBuilder();
        List<EdsInvoicePayment> edsInvoicePaymentList = invoicePaymentManager.getBatchPaymentItems(requestObject.getObjectID());

        invoiceItemsData.addColumnOrder(
                "NO",
                "INVOICE_PRODUCT_NAME",
                "INVOICE_PRODUCT_TYPE",
                "INVOICE_ITEM_NAMES",
                "INVOICE_STATUS",
                "INVOICE_NET_AMOUNT",
                "INVOICE_ITEM_TAX_AMOUNT",
                "INVOICE_ITEM_TAX_RATE",
                "ITEM_TAX_OF_PAYMENT_AMOUNT",
                "ITEM_TOTAL_PAYMENT_AMOUNT",
                "AMOUNT");

        String itemNamesString = "";
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            if (edsInvoicePayment.getInvoice() != null && edsInvoicePayment.getInvoice().getInvoiceItems() != null && edsInvoicePayment.getInvoice().getInvoiceItems().size() > 0) {
                for (EdsInvoiceItem invoiceItem : edsInvoicePayment.getInvoice().getInvoiceItems()) {
                    String itemName = invoiceItem.getItem() != null ? invoiceItem.getItem().getName() : "";
                    itemNames.append(itemName).append(", ");
                    itemNamesString = itemNames.toString().replaceAll(", $", "");
                }
            }
        }

        int count = 0;
        for (EdsInvoicePayment edsInvoicePayment : edsInvoicePaymentList) {
            BigDecimal paymentAmount = edsInvoicePayment.getAmount();
            if (edsInvoicePayment.getInvoice() != null && edsInvoicePayment.getInvoice().getInvoiceItems() != null && edsInvoicePayment.getInvoice().getInvoiceItems().size() > 0) {
                for (EdsInvoiceItem invoiceItem : edsInvoicePayment.getInvoice().getInvoiceItems()) {
                    count = count + 1;
                    String itemName = invoiceItem.getItem() != null ? invoiceItem.getItem().getName() : "";
                    String itemType = invoiceItem.getItem() != null ? invoiceItem.getItem().getTypeName() : "";
                    String invoiceStatusCode = invoiceItem.getInvoice() != null && invoiceItem.getInvoice().getStatus() != null ? invoiceItem.getInvoice().getStatus().getCode() : "";

                    BigDecimal taxPercent = invoiceItem.getVat() != null && invoiceItem.getVat().createTaxItem() != null && invoiceItem.getVat().createTaxItem().getEffectiveTaxPercent() != null ? invoiceItem.getVat().createTaxItem().getEffectiveTaxPercent() : ZERO;
                    BigDecimal taxAmountOfPaymentAmount = BigDecimal.ZERO;
                    BigDecimal totalPaymentAmount = paymentAmount != null ? paymentAmount : BigDecimal.ZERO;
                    if (invoiceItem.getTaxCalculationType() != null && paymentAmount != null) {
                        if (TAX_CALCULATION_INCLUSIVE.equals(invoiceItem.getTaxCalculationType())) {
                            taxAmountOfPaymentAmount = paymentAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, BigDecimal.ROUND_HALF_UP);
                        } else if (TAX_CALCULATION_EXCLUSIVE.equals(invoiceItem.getTaxCalculationType())) {
                            taxAmountOfPaymentAmount = paymentAmount.multiply(taxPercent).divide(HUNDRED, 4, BigDecimal.ROUND_HALF_UP);
                            totalPaymentAmount = totalPaymentAmount.add(taxAmountOfPaymentAmount);
                        }
                    }

                    invoiceItemsData.addRow(
                            count + ".",
                            itemName,
                            itemType,
                            itemNamesString,
                            invoiceStatusCode,
                            defaultScaleFormat.format(invoiceItem.getNet() != null ? invoiceItem.getNet() : BigDecimal.ZERO),
                            defaultScaleFormat.format(invoiceItem.getTaxAmount() != null ? invoiceItem.getTaxAmount() : BigDecimal.ZERO),
                            defaultScaleFormat.format(taxPercent),
                            defaultScaleFormat.format(taxAmountOfPaymentAmount),
                            defaultScaleFormat.format(totalPaymentAmount),
                            defaultScaleFormat.format(paymentAmount != null ? paymentAmount : BigDecimal.ZERO)
                    );

                    //invoice product custom fields
                    LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                    if (invoiceItem.getItem() != null && invoiceItem.getItem().getCustomFields() != null) {
                        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(invoiceItem.getItem().getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView));
                        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                            if (customFieldItem != null) {
                                Map<String, String> cols = new HashMap<>();
                                cols.put(COLUMN_NAME, customFieldItem.getFieldName() != null ? escapeHtml(customFieldItem.getFieldName()) : null);
                                if (CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                                    cols.put(COLUMN_VALUE, customFieldItem.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                                } else {
                                    cols.put(COLUMN_VALUE, customFieldItem.getFieldStringValue() != null ? escapeHtml(customFieldItem.getFieldStringValue()) : null);
                                }
                                if (customFieldItem.getFieldName() != null) {
                                    itemCusFields.put(escapeHtml(customFieldItem.getFieldName()), cols);
                                }
                            }
                        }
                    }
                    customFields.put(count + ".", itemCusFields);

                }
            }
        }

        invoiceItemsData.setCustomFields(getCustomFields(null, customFields));

        return invoiceItemsData;
    }

    private ITextUserData getCreatorData(EdsUser user) {
        ITextUserData result = new ITextUserData();
        if (user != null) {
            result.setFullName(escapeHtml(user.getFullName()));
            result.setEmail(escapeHtml(user.getEmail()));
        }
        return result;
    }

    private CustomisedITextTable getCurrentData() {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String dateValue = "";
        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
        if (company.getLocale() != null && "ru".equals(company.getLocale())) {
            Locale ruLocale = new Locale("ru", "RU");
            SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
            dateValue = ruDateFormat.format(currentDate);
        } else {
            dateValue = shortDateFormat.format(currentDate);
        }

        customTable.addRow(CURRENT_DATE, dateValue);
        customTable.addRow("CURRENT_YEAR", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRow("CURRENT_TIME", timeFormat.format(userManager.getUser().getUserDate()));

        return customTable;
    }

    private CustomisedITextTable getCustomisedBankTableData(SelectItem bankAccount) {
        CustomisedITextTable bankAccountTable = new CustomisedITextTable();
        bankAccountTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        EdsBankAccount edsBankAccount = null;
        if (bankAccount != null && bankAccount.getId() != null) {
            edsBankAccount = bankAccountManager.get(bankAccount.getId());
        }
        if (edsBankAccount != null) {
            if (isValid(edsBankAccount.getAccount() != null ? edsBankAccount.getAccount().getName() : "")) {
                bankAccountTable.addRowWithCode(BANK_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bank), escapeHtml(edsBankAccount.getAccount().getName()), BANK_NAME);
            }
            if (isValid(edsBankAccount.getBankBranch())) {
                bankAccountTable.addRowWithCode(BRANCH, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.branch), escapeHtml(edsBankAccount.getBankBranch()), BRANCH);
            }
            if (isValid(edsBankAccount.getStreetAddress()) || isValid(edsBankAccount.getCity()) || isValid(edsBankAccount.getCountry() != null ? edsBankAccount.getCountry().getName() : "") || isValid(edsBankAccount.getPostCode())) {
                String bankAddress = "";
                if (edsBankAccount.getStreetAddress() != null && !edsBankAccount.getStreetAddress().equals("")) {
                    bankAddress = bankAddress + edsBankAccount.getStreetAddress() + "\n";

                    bankAccountTable.addRowWithCode(STREET_ADDRESS, accountingLocalizer.localizeAccounting(PdfLocalizationName.streetAddress), escapeHtml(edsBankAccount.getStreetAddress()), STREET_ADDRESS);
                }
                bankAddress = bankAddress + (((edsBankAccount.getCity() != null && !edsBankAccount.getCity().equals("")) ? edsBankAccount.getCity() + ", " : "") +
                                             ((edsBankAccount.getState() != null && edsBankAccount.getState().getName() != null) ? edsBankAccount.getState().getName() + " " : "") +
                                             ((edsBankAccount.getPostCode() != null && !edsBankAccount.getPostCode().equals("")) ? edsBankAccount.getPostCode() : "")) + "\n";

                if (edsBankAccount.getCity() != null && !"".equals(edsBankAccount.getCity())) {
                    bankAccountTable.addRowWithCode(BANK_ACCOUNT_CITY, accountingLocalizer.localizeAccounting(PdfLocalizationName.city), escapeHtml(edsBankAccount.getCity()), BANK_ACCOUNT_CITY);
                }

                if (edsBankAccount.getState() != null && edsBankAccount.getState().getName() != null && !"".equals(edsBankAccount.getState().getName())) {
                    bankAccountTable.addRowWithCode(BANK_ACCOUNT_STATE, accountingLocalizer.localizeAccounting(PdfLocalizationName.state), escapeHtml(edsBankAccount.getState().getName()), BANK_ACCOUNT_STATE);
                }

                if (edsBankAccount.getPostCode() != null && !"".equals(edsBankAccount.getPostCode())) {
                    bankAccountTable.addRowWithCode(BANK_ACCOUNT_POSTCODE, accountingLocalizer.localizeAccounting(PdfLocalizationName.postCode), escapeHtml(edsBankAccount.getPostCode()), BANK_ACCOUNT_POSTCODE);
                }

                if (edsBankAccount.getCountry() != null && edsBankAccount.getCountry().getName() != null) {
                    bankAddress = bankAddress + edsBankAccount.getCountry().getName();

                    bankAccountTable.addRowWithCode(BANK_ACCOUNT_COUNTRY, accountingLocalizer.localizeAccounting(PdfLocalizationName.country), escapeHtml(edsBankAccount.getCountry().getName()), BANK_ACCOUNT_COUNTRY);
                }
                bankAccountTable.addRowWithCode(BILL_ADDRESS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bankAddress), escapeHtml(bankAddress), BILL_ADDRESS);
            }
            if (isValid(edsBankAccount.getPhoneNumber())) {
                bankAccountTable.addRowWithCode(PHONE_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.phone), escapeHtml(edsBankAccount.getPhoneNumber()), PHONE_NUMBER);
            }
            if (edsBankAccount.getAccount() != null && edsBankAccount.getAccount().getCurrency() != null) {
                bankAccountTable.addRowWithCode(CURRENCY, accountingLocalizer.localizeAccounting(PdfLocalizationName.currency), escapeHtml(edsBankAccount.getAccount().getCurrency().getName()), CURRENCY);
            }

            bankAccountTable.setCustomFields(getCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(edsBankAccount.getCustomFields(), commonService.getCompanyCustomFields(ViewName.BankAccounts)), null));
        }
        return bankAccountTable;
    }

    private CustomisedITextTable getCustomisedAccountTableData(SelectItem bankAccount) {
        CustomisedITextTable bankAccountTable = new CustomisedITextTable();
        bankAccountTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        EdsBankAccount edsBankAccount = null;
        if (bankAccount != null && bankAccount.getId() != null) {
            edsBankAccount = bankAccountManager.get(bankAccount.getId());
        }
        if (edsBankAccount != null) {
            if (isValid(edsBankAccount.getAccauntName())) {
                bankAccountTable.addRowWithCode(ACCOUNT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountName), escapeHtml(edsBankAccount.getAccauntName()), ACCOUNT_NAME);
            }
            if (isValid(edsBankAccount.getAccountNumber())) {
                bankAccountTable.addRowWithCode(ACCOUNT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.accountNo),escapeHtml(edsBankAccount.getAccountNumber()), ACCOUNT_NUMBER);
            }
            if (isValid(edsBankAccount.getSwiftCode())) {
                bankAccountTable.addRowWithCode(SWIFT_BIC, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.swiftBic), escapeHtml(edsBankAccount.getSwiftCode()), SWIFT_BIC);
            }
            if (isValid(edsBankAccount.getSortCode())) {
                bankAccountTable.addRowWithCode(SORT_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.sortCode), escapeHtml(edsBankAccount.getSortCode()), SORT_CODE);
            }
            if (isValid(edsBankAccount.getIbanCode())) {
                bankAccountTable.addRowWithCode(IBAN_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.ibanCode), escapeHtml(edsBankAccount.getIbanCode()), IBAN_CODE);
            }
            if (isValid(edsBankAccount.getAbaCode())) {
                bankAccountTable.addRowWithCode(ABA_CODE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.abaCode), escapeHtml(edsBankAccount.getAbaCode()), ABA_CODE);
            }
        }
        return bankAccountTable;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(List<CompanyCustomFieldItem> bankCustomFields, Map<String, LinkedHashMap<String, Map<String, String>>> customFields) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }

        if (bankCustomFields != null && bankCustomFields.size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : bankCustomFields) {
                if (item != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                        cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                    } else {
                        cols.put(COLUMN_VALUE, escapeHtml(item.getFieldStringValue()));
                    }
                    if (item.getFieldName() != null) {
                        itemCusFields.put(item.getFieldName(), cols);
                    }
                }
            }
            customFields.put(PDFConstants.INVOICE, itemCusFields);
        }
        return customFields;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        boolean isReceivable = Constants.RECEIVABLE.equals(requestObject.getViewName());
        EdsCompany company = userManager.getUser().getCompany();
        ReceivePaymentData batchPaymentData = invoiceServiceLocal.getBatchPaymentPdfData(requestObject.getObjectID());
        String number = batchPaymentData != null ? escapeHtml(batchPaymentData.getNumber()) : "";
        String companyName = company != null ? company.getName() : "";
        setFileName(isReceivable ? companyName + "_" + number : "Payment Voucher");
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        if (!(dataClass instanceof TransactionPDFObject)) {
            return super.getPdfCodeName(dataClass);
        }
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        if (Constants.RECEIVABLE.equals(requestObject.getViewName())) {
            return PdfReferenceCodeNameEnum.BATCH_RECEIVE_PAYMENT;
        } else {
            return PdfReferenceCodeNameEnum.BATCH_PAY_BILL;
        }
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        TransactionPDFObject requestObject = new TransactionPDFObject();
        if (!request.getParameter("ids").equals("")) {
            requestObject.setObjectID(Integer.valueOf(request.getParameter("ids").split(",")[0]));
        } else {
            requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        }
        if (!"".equals(request.getParameter("templateID"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateID")));
        }
        requestObject.setViewName(request.getParameter("viewName"));
        return requestObject;
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
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof TransactionPDFObject) {
            return ((TransactionPDFObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    private boolean isValid(String value) {
        return value != null && value.length() > 0;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (!(dataClass instanceof TransactionPDFObject)) {
            return super.getTableName(dataClass);
        }
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        if (Constants.RECEIVABLE.equals(requestObject.getViewName())) {

            return commonLocalizer.localize(PdfLocalizationName.paymentReceipt);
        } else {
            return commonLocalizer.localize(PdfLocalizationName.paymentVoucher);
        }
    }
}
